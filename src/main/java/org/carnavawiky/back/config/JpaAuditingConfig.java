package org.carnavawiky.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Habilita la auditoría de JPA
public class JpaAuditingConfig {
    // Esta clase activa los campos @CreatedDate y @LastModifiedDate
}