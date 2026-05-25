package com.example.final_project.service;

import com.example.final_project.entity.Student;
import com.example.final_project.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    
    public Student update(Long id, Student student) {
    Student existing = getById(id);
    existing.setName(student.getName());
    existing.setGroupName(student.getGroupName()); 
    return studentRepository.save(existing);
}

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}