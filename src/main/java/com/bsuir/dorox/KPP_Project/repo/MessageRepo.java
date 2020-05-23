package com.bsuir.dorox.KPP_Project.repo;

import com.bsuir.dorox.KPP_Project.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
