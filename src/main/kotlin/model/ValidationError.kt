package model

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValidationError (
    private val code: String? = null,
    private val arguments: List<String>? = null
)