package com.tibebues.enb.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tibebues.enb.models.Notice;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{
	//returns notices where closingDate >= today && openingDate<=today
	@Query(value = "SELECT * FROM NOTICES WHERE ?1 BETWEEN opening_date AND closing_date",nativeQuery = true)
	List<Notice> findNoticesForToday(Date today);

}
