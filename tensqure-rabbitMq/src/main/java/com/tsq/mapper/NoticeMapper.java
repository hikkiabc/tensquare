package com.tsq.mapper;

import com.tsq.beans.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeMapper extends JpaRepository<Notice,String> {
}
