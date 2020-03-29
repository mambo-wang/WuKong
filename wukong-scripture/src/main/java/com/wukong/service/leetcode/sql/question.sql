

-- 基于boss.sql
-- 题目1：查询发表时间在18年5月份的发表量排行榜
SELECT u.name,COUNT(*) AS count FROM user u  INNER JOIN blog b WHERE u.id=b.user_id AND YEAR(b.add_time)=2018 AND MONTH(b.add_time)=5 GROUP BY b.user_id ORDER BY COUNT desc