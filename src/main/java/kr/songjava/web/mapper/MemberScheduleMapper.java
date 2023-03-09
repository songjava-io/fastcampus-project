package kr.songjava.web.mapper;

import kr.songjava.web.domain.MemberSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * packageName :  kr.songjava.web.mapper
 * fileName : MemberScheduleMapper
 * author :  82108
 * date : 2023-03-09
 * description :
 */
public interface MemberScheduleMapper {

    List<MemberSchedule> getList(@Param("memberSeq") int memberSeq);

    MemberSchedule get(int scheduleSeq);

    void save(MemberSchedule schedule);

    void update(MemberSchedule schedule);

    void delete(int scheduleSeq);

}
