package kr.songjava.web.mapper;

import kr.songjava.web.domain.MemberSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberScheduleMapper {

	List<MemberSchedule> getList(@Param("memberSeq") int memberSeq);

	MemberSchedule get(int scheduleSeq);

	void save(MemberSchedule schedule);

	void update(MemberSchedule schedule);

	void delete(int scheduleSeq);

}
