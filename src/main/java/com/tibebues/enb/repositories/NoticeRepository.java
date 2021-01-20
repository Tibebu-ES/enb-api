package com.tibebues.enb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tibebues.enb.models.Notice;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{

}
