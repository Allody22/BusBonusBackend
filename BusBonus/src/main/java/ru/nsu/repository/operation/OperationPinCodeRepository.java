package ru.nsu.repository.operation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.model.operations.OperationPincode;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationPinCodeRepository extends JpaRepository<OperationPincode, Long> {

    @Query(value = "SELECT * FROM operation_pincode op " +
            "JOIN operation_code_names ocl ON op.operation_name = ocl.id JOIN code_operation_direction cod on op.operation_direction = cod.id " +
            "WHERE op.login_user = :loginUser " +
            "AND op.date > :startDate AND ocl.name = :operationName AND cod.direction = :operationDirection " +
            "ORDER BY op.date DESC LIMIT 1", nativeQuery = true)
    Optional<OperationPincode> findTopByLoginUserAndDateAfterAndOperationDirection(
            @Param("loginUser") String loginUser,
            @Param("operationName") String operationName,
            @Param("operationDirection") String operationDirection,
            @Param("startDate") Date startDate);


    @Query(value = "SELECT * FROM operation_pincode op " +
            "JOIN operation_code_names ocl ON op.operation_name = ocl.id JOIN code_operation_direction cod on op.operation_direction = cod.id " +
            "WHERE op.login_user = :loginUser " +
            "AND op.date > :startDate AND ocl.name = :operationName AND cod.direction = :operationDirection " +
            "ORDER BY op.date DESC", nativeQuery = true)
    List<OperationPincode> findAllByLoginUserAndDateAfterAnAndOpRefOperationDirection(
            @Param("loginUser") String loginUser,
            @Param("operationName") String operationName,
            @Param("operationDirection") String operationDirection,
            @Param("startDate") Date startDate);

    @Query(value = "SELECT * FROM operation_pincode op " +
            "JOIN operation_code_names ocl ON op.operation_name = ocl.id JOIN code_operation_direction cod on op.operation_direction = cod.id " +
            "WHERE op.login_user LIKE %:loginUser% " +
            "AND op.date > :startDate AND ocl.name = :operationName AND cod.direction = :operationDirection " +
            "ORDER BY op.date DESC", nativeQuery = true)
    List<OperationPincode> findAllByLikeLoginUserAndDateAfterAnAndOpRefOperationDirection(
            @Param("loginUser") String loginUser,
            @Param("operationName") String operationName,
            @Param("operationDirection") String operationDirection,
            @Param("startDate") Date startDate);
}
