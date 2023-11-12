package com.example.thymeleaf.dto.mapper;

import com.example.thymeleaf.dto.CreateStudentDTO;
import com.example.thymeleaf.dto.StudentResponseDTO;
import com.example.thymeleaf.entity.Address;
import com.example.thymeleaf.entity.Student;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentMapper {

    public static Student toEntity(CreateStudentDTO dto) {
        Address address = new Address(
                dto.getZipCode(),
                dto.getStreet(),
                dto.getNumber(),
                dto.getComplement(),
                dto.getDistrict(),
                dto.getCity(),
                dto.getState()
        );
        Student student = new Student(dto.getName(), dto.getEmail(), dto.getBirthday(), address);
        address.setStudent(student);
        return student;
    }

    public static StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setBirthday(student.getBirthday());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setZipCode(student.getAddress().getZipCode());
        dto.setStreet(student.getAddress().getStreet());
        dto.setNumber(student.getAddress().getNumber());
        dto.setComplement(student.getAddress().getComplement());
        dto.setDistrict(student.getAddress().getDistrict());
        dto.setCity(student.getAddress().getCity());
        dto.setState(student.getAddress().getState());

        return dto;
    }

    public static List<StudentResponseDTO> toDTO(List<Student> students) {
        return students.stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

}
