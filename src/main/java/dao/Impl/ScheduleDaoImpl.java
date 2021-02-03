package dao.Impl;

import dao.ScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/25 10:23
 */
@Repository
public class ScheduleDaoImpl implements ScheduleDao {
    @Autowired
    private JdbcTemplate jt;

}
