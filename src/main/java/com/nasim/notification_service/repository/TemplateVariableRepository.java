package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.TemplateVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateVariableRepository extends JpaRepository<TemplateVariable,Long> {
    @Query("""
select tmpv from TemplateVariable tmpv join tmpv.template tmp
where tmp.id= :templateId and tmp.active=true and tpmv.active=true
""")
    List<TemplateVariable> findAllTemplateVariableByTemplateId(Long templateId);

}
