package com.skill_mentor.root.dao;

import com.skill_mentor.root.dto.StudentDTO;
import com.skill_mentor.root.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAOimpl implements StudentDAO {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            final String sql = "Insert into student(first_name, last_name, email, phone_no, address, age) values(?,?,?,?,?,?)";
            connection = databaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, studentDTO.getFirstName());
            preparedStatement.setString(2, studentDTO.getLastName());
            preparedStatement.setString(3, studentDTO.getEmail());
            preparedStatement.setString(4, studentDTO.getPhoneNumber());
            preparedStatement.setString(5, studentDTO.getAddress());
            preparedStatement.setInt(6, studentDTO.getAge());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(connection != null) connection.close();
                if(preparedStatement != null) preparedStatement.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return studentDTO;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        final List<StudentDTO> studentDTOs = new ArrayList<>();
        final String sql = "Select * from student";
        try(
            Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()
        ){
            while(resultSet.next()) {
                final StudentDTO studentDTO = new StudentDTO(
                        resultSet.getInt("student_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_no"),
                        resultSet.getString("address"),
                        resultSet.getInt("age")
                );
                studentDTOs.add(studentDTO);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return studentDTOs;
    }

    @Override
    public StudentDTO getStudentById(Integer id) {
        return null;
    }

    @Override
    public StudentDTO updateStudentById(Integer id, StudentDTO studentDTO) {
        return null;
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        return false;
    }
}
