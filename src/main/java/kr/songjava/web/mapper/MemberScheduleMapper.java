package kr.songjava.web.mapper;

import kr.songjava.web.domain.MemberSchedule;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberScheduleMapper {

    List<MemberSchedule> getList(@Param("memberSeq") int memberSeq);

    MemberSchedule get(int scheduleSeq);

    void save(MemberSchedule memberSchedule);

    void update(MemberSchedule memberSchedule);

    void delete(int scheduleSeq);

}
