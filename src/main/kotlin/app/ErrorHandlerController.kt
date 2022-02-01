package app

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import model.ValidationErrors
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@RestControllerAdvice
class ErrorHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationError(
        exception: MethodArgumentNotValidException): ValidationErrors {

            val errors: MutableList<FieldError?> = exception.bindingResult.fieldErrors
            val result = ValidationErrors()
            for (error in errors) {
                result.addFieldError(error!!)
            }
            return result
    }
}