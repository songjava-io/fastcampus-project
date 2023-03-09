package kr.songjava.web.mapper;

import kr.songjava.web.domain.MemberSchedule;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberScheduleMapper {
    List<MemberSchedule> getList(@Param("memberSeq") int memberSeq);

    void save(MemberSchedule memberSchedule);

    MemberSchedule get(int scheduleSeq);

    void update(MemberSchedule memberSchedule);

    void delete(int scheduleSeq);

}
