package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.mapper.*;
import cn.bestzuo.mjforum.pojo.*;
import cn.bestzuo.mjforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Collection;

/**
 * 问题管理Service
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    private final QuestionTagMapper questionTagMapper;

    private final TagMapper tagMapper;

    private final UserRateMapper userRateMapper;

    private final UserInfoMapper userInfoMapper;

    private final CommentMapper commentMapper;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper, QuestionTagMapper questionTagMapper, TagMapper tagMapper, UserRateMapper userRateMapper, UserInfoMapper userInfoMapper, CommentMapper commentMapper) {
        this.questionMapper = questionMapper;
        this.questionTagMapper = questionTagMapper;
        this.tagMapper = tagMapper;
        this.userRateMapper = userRateMapper;
        this.userInfoMapper = userInfoMapper;
        this.commentMapper = commentMapper;
    }


    /**
     * 查询所有问题信息
     * 将有置顶的放在最前面
     * 并且按照最后回复时间进行排序
     *
     * @return 问题信息
     */
    @Override
    public List<Question> queryAllQuestionsWithCurrPage(Integer currPage) throws ParseException {
        List<Question> reOrderList = new ArrayList<>();  //最终结果容器

        //存储最后回复时间，没有回复的存储发布时间
        Map<Question, Date> map = new HashMap<>();

        List<Question> questionList = questionMapper.getAllQuestions();

        for (Question question : questionList) {
            if (question.getIsDing() != 1) {
                //非置顶问题
                //查询时间，有最后回复时间的按照最后回复时间，没有的按照发帖时间
                List<Comment> comments = commentMapper.queryCommentByQuestionId(question.getId());
                if (comments.size() > 0) {
                    //说明这个问题有评论，找最后一条评论的时间，存储进map
                    map.put(question, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(comments.get(comments.size() - 1).getTime()));
                } else {
                    //说明这个问题没有评论信息，直接放发布时间
                    map.put(question, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(question.getGmtCreate()));
                }
            }
        }

        //再按照最后回复时间排序
        List<Map.Entry<Question, Date>> res = new ArrayList<>((map.entrySet()));
        res.sort(Comparator.comparingLong(o -> o.getValue().getTime()));

        //排序后的结果
        for (Map.Entry<Question, Date> ent : res) {
            reOrderList.add(ent.getKey());
        }

        //先找到置顶的问题加进去
        List<Question> dingQuestions = questionMapper.getDingQuestions();

        for (Question question : dingQuestions) {
            reOrderList.add(question);
        }

        Collections.reverse(reOrderList);

        return reOrderList;
    }

    /**
     * 查询所有问题
     *
     * @return 问题信息
     */
    @Override
    public List<Question> queryAllQuestions() {
        return questionMapper.getAllQuestions();
    }


    /**
     * 推荐
     *
     * @return 问题信息
     */
    @Override
    public List<Question> getAllQuestionsByViewCount() {
        return questionMapper.getAllQuestionsByViewCount();
    }

    /**
     * 热门
     *
     * @return 问题信息
     */
    @Override
    public List<Question> getAllQuestionsByCommentCount() {
        return questionMapper.getAllQuestionsByCommentCount();
    }

    /**
     * 消灭0回复
     *
     * @return 问题信息
     */
    @Override
    public List<Question> getAllQuestionsByZeroComment() {
        return questionMapper.getAllQuestionsByZeroComment();
    }

    /**
     * 新增问题，需要插入以下数据库：
     * 1. tb_question      //问题表
     * 2. tb_question_tag  //问题和标签的关系
     * 3. tb_tags          //标签表
     * 4. tb_category_tag   //标签分类和标签的关系
     * <p>
     * 2020.4.29 发布问题时不再新增数据库中没有的标签
     *
     * @param question 问题信息
     */
    @Override
    @Transactional
    public void insertQuestion(Question question) {
        //插入问题，返回主键
        questionMapper.insertSelective(question);

        //返回的问题主键ID
        int questionId = question.getId();

        //查询数据库中的所有标签信息
        List<Tags> tagsList = tagMapper.queryAllTags();

        //获取问题中对应的标签
        String s = question.getTag();

        if (s.indexOf(",") > 0) {
            //存在多个标签，进行拆解
            String[] tags = s.split(",");

            //将数据库中所有标签放入容器
            List<String> tagList = new ArrayList<>();
            for (Tags t : tagsList) {
                tagList.add(t.getTagsName());
            }

            //对每个标签进行分类，如果当前标签库中没有这个标签，将其插入到数据库中
            Map<String, Integer> map = new LinkedHashMap<>();
            for (String t : tags) {
                if (!tagList.contains(t)) {
                    map.put(t, 6);  //默认放到“其它”类别中，6代表其它类别
                }
            }

            //新增数据库中没有的标签
            for (Map.Entry<String, Integer> m : map.entrySet()) {
                //插入标签表
                Tags res = new Tags();
                res.setTagsName(m.getKey());
                res.setCategoryId(m.getValue());
                res.setIsOriginTag(0);  //设置为非默认标签  0-非默认
                tagMapper.insertNewTag(res);

                //插入分类和标签的对应关系
                // 2020.4.29 分类和标签的关系保持不变
                tagMapper.insertQuestionAndTag(res.getId(), question.getId());
            }

            //插入问题和标签的关系
            for (String t : tags) {
                //查询标签ID
                QuestionTag questionTag = new QuestionTag();
                questionTag.setTagId(tagMapper.selectTagIdByTagName(t));
                questionTag.setQuestionId(questionId);
                questionTagMapper.insertQuestionTag(questionTag);
            }

            //新增用户积分
            UserInfo userInfo = userInfoMapper.selectUserInfoByUid(question.getPublisherId());
            UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());
            if (userRate == null) {
                //新增用户积分信息
                UserRate rate = new UserRate();
                rate.setUserId(userInfo.getUId());
                rate.setRate(3);
                userRateMapper.insertUserRate(rate);
            } else {
                int rate = userRate.getRate();
                //发一贴加3积分
                userRateMapper.updateRateById(userInfo.getUId(), rate + 3);
            }

        } else {
            //只存在一个标签
            List<String> tagList = new ArrayList<>();
            for (Tags t : tagsList) {
                tagList.add(t.getTagsName());
            }
            if (!tagList.contains(s)) {
                Tags res = new Tags();
                res.setTagsName(s);
                res.setCategoryId(6);
                res.setIsOriginTag(0);
                tagMapper.insertNewTag(res);

                //插入问题和标签的关系
                tagMapper.insertQuestionAndTag(res.getId(), question.getId());
            }

            //插入问题和标签对应的关系
            QuestionTag questionTag = new QuestionTag();
            questionTag.setTagId(tagMapper.selectTagIdByTagName(s));
            questionTag.setQuestionId(questionId);
            questionTagMapper.insertQuestionTag(questionTag);

            //新增用户积分
            UserInfo userInfo = userInfoMapper.selectUserInfoByUid(question.getPublisherId());
            UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());
            if (userRate == null) {
                //新增用户积分信息
                UserRate rate = new UserRate();
                rate.setUserId(userInfo.getUId());
                rate.setRate(3);
                userRateMapper.insertUserRate(rate);
            } else {
                int rate = userRate.getRate();
                //发一贴加3积分
                userRateMapper.updateRateById(userInfo.getUId(), rate + 3);
            }
        }
    }

    /**
     * 根据主键查询问题
     *
     * @param id 问题ID
     * @return 问题信息
     */
    @Override
    public Question selectByPrimaryKey(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新阅读量
     *
     * @param record 问题信息
     * @return 更新的行数
     */
    @Override
    @Transactional
    public int updateByPrimaryKeySelective(Question record) {
        Question question = questionMapper.selectByPrimaryKey(record.getId());
        question.setViewCount(question.getViewCount() + 1);

        return questionMapper.updateByPrimaryKeySelective(question);
    }

    /**
     * 根据问题ID更新问题信息
     * 在Controller已经对数据判空进行了校验，此处还需要校验格式，-1——数据格式不正确，0——更新失败，1——更新成功
     */
    @Override
    @Transactional
    public int updateQuestionInfoByIdSelective(Integer questionId, String description, String tag) {
        //查询该问题ID对应的原始标签信息
        List<Integer> tagIds = questionTagMapper.selectTagByQuestionId(questionId);

        //查询数据库中所有的tag信息
        List<Tags> tagsList = tagMapper.queryAllTags();

        //表示标签中只能包含汉字、字母和数字
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";

        //校验标签信息以及对标签与问题信息进行操作
        if (tag.contains(",")) {
            //包含多个tag信息
            //1.校验是否出现除数字、英文字母、汉字之外的其它特殊字符
            String[] tags = tag.split(",");
            for (String s : tags) {
                if (!s.matches(regex))
                    return -1;  //说明输入格式有误
            }
            //2.说明格式正确，核对数据库中的标签信息，并对修改后的标签进行删除
            //多标签之间进行删除操作
            List<Integer> newTagIdList = new ArrayList<>();
            for (String s : tags) {
                Integer tagId = tagMapper.selectTagIdByTagName(s);
                if (tagId == null) {
                    //新增该tag信息
                    Tags t = new Tags();
                    t.setTagsName(s);
                    t.setCategoryId(6);
                    t.setIsOriginTag(0);
                    tagMapper.insertNewTag(t);

                    //将返回的tagId主键放入list中
                    newTagIdList.add(t.getId());

                    //新增问题与标签的对应关系
                    QuestionTag qt = new QuestionTag();
                    qt.setQuestionId(questionId);
                    qt.setTagId(t.getId());
                    questionTagMapper.insertQuestionTag(qt);
                } else {
                    newTagIdList.add(tagId);
                }
            }
            //删除新更新的标签集中对应原始标签中不存在的标签
            for (Integer id : tagIds) {
                if (!newTagIdList.contains(id))
                    questionTagMapper.deleteQuestionAndTagInfo(questionId, id);
            }
        } else {
            //仅包含一个标签信息
            Integer originTagId = tagMapper.selectTagIdByTagName(tag);

            if (!tag.matches(regex)) return -1;

            List<Integer> tagIdsList = new ArrayList<>();
            for (Tags t : tagsList) {
                tagIdsList.add(t.getId());
            }

            //说明格式正确，查询该标签是否已经在数据库中，如果没有则新增，并将原始的标签信息删除
            if (!tagIdsList.contains(originTagId)) {
                //新增tag标签
                Tags t = new Tags();
                t.setTagsName(tag);
                t.setCategoryId(6);
                t.setIsOriginTag(0);
                int i = tagMapper.insertNewTag(t);


                //新标签，说明原始的标签信息被修改，需要删除原始问题和标签的对应关系
                for (Integer tagId : tagIds) {
                    questionTagMapper.deleteQuestionAndTagInfo(questionId, tagId);
                }

                //然后新增问题和标签的对应关系
                QuestionTag qt = new QuestionTag();
                qt.setQuestionId(questionId);
                qt.setTagId(t.getId());
                questionTagMapper.insertQuestionTag(qt);
            } else {
                //不是新标签，查看原始问题对应标签中是否包含该标签
                if (tagIds.contains(originTagId)) {
                    //如果标签库中包含了这个标签，那么只删除原始多余的标签即可
                    for (Integer tagId : tagIds) {
                        if (tagId != originTagId) {
                            //删除操作
                            questionTagMapper.deleteQuestionAndTagInfo(questionId, tagId);
                        }
                    }
                } else {
                    //原始问题信息中没有包含该标签，需要删除原始所有标签，并新增标签信息
                    for (Integer tagId : tagIds) {
                        //删除操作
                        questionTagMapper.deleteQuestionAndTagInfo(questionId, tagId);
                    }
                    //新增问题与标签对应信息
                    QuestionTag qt = new QuestionTag();
                    qt.setQuestionId(questionId);
                    qt.setTagId(originTagId);
                    questionTagMapper.insertQuestionTag(qt);
                }
            }
        }

        //更新问题信息
        Question question = questionMapper.selectByPrimaryKey(questionId);
        question.setTag(tag);
        question.setDescription(description);

        //更新问题的修改日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        question.setGmtModified(sdf.format(date));

        return questionMapper.updateQuestionInfoByIdSelective(question);
    }

    /**
     * 删除问题
     * 1.删除问题
     * 2.删除问题与标签的关系
     * 3.删除该问题下的所有评论信息
     * 4.删除该问题的收藏信息
     * 5.删除评论中获赞的信息
     *
     * @param questionId 问题ID
     * @param userId     用户ID
     * @return 通用结果
     */
    @Override
    public ForumResult deleteQuestion(Integer questionId, Integer userId) {
        //TODO
        return null;
    }

    /**
     * 根据问题浏览量信息查询问题信息
     *
     * @return 问题信息
     */
    @Override
    public List<Question> selectQuestionInfoByViewCount() {
        return questionMapper.selectQuestionInfoByViewCount();
    }

    /**
     * 根据问题ID查询标签
     *
     * @param questionId 问题ID
     * @return 标签名
     */
    @Override
    public String queryTagByQuestionId(Integer questionId) {
        return questionMapper.queryTagByQuestionId(questionId);
    }

    /**
     * 根据发布者查询发布的问题信息
     *
     * @param publisher 用户
     * @return 问题信息
     */
    @Override
    public List<Question> getAllQuestionsByPublisher(String publisher) {
        return questionMapper.getAllQuestionsByPublisherId(userInfoMapper.selectUserInfoByName(publisher).getUId());
    }

    /**
     * 获取我的最热问题
     *
     * @param username 用户名
     * @return 问题信息
     */
    @Override
    public List<Question> selectMyHotQuestions(String username) {
        return questionMapper.selectMyHotQuestions(userInfoMapper.selectUserInfoByName(username).getUId());
    }

    /**
     * 查询论坛推荐问题
     *
     * @return 问题信息
     */
    @Override
    public List<Question> selectForumRecommendQuestions() {
        return questionMapper.selectForumRecommendQuestions();
    }

    /**
     * 获取所有精品帖子
     *
     * @return 问题信息
     */
    @Override
    public List<Question> getAllJingQuestions() {
        return questionMapper.getJingQuestions();
    }

    /**
     * 更新like_count
     *
     * @param likeCount  收藏数
     * @param questionId 问题ID
     * @return 更新行数
     */
    @Override
    public int updateLikeCountById(Integer likeCount, Integer questionId) {
        return questionMapper.updateLikeCountById(likeCount, questionId);
    }
}
