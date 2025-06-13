package com.example.keshe.mapper;
import com.example.keshe.entity.ApplyForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MApplyMapper {
    void insertApply_Form(ApplyForm applyForm);
}
