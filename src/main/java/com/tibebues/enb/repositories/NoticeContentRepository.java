package com.tibebues.enb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tibebues.enb.models.NoticeContent;

@Repository
public interface NoticeContentRepository extends JpaRepository<NoticeContent, Long>{
	List<NoticeContent> findByNoticeId(Long noticeId);

}
