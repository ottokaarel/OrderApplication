package model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.validation.FieldError
import org.springframework.context.support.DefaultMessageSourceResolvable
import java.util.ArrayList
import java.util.stream.Collectors
import java.util.stream.Stream

class ValidationErrors {

    @JsonProperty
    private val errors: MutableList<ValidationError> = ArrayList()

    fun addErrorMessage(message: String?) {
        errors.add(ValidationError(message!!))
    }

    fun addFieldError(fieldError: FieldError) {
        val args = Stream.of(*fieldError.arguments)
            .filter { arg: Any? -> arg !is DefaultMessageSourceResolvable }
            .map { obj: Any -> obj.toString() }
            .collect(Collectors.toList())
        errors.add(ValidationError(fieldError.codes[0], args))
    }

    fun hasErrors(): Boolean {
        return !errors.isEmpty()
    }
}