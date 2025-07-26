package com.tohid.secretstash.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SizeCharArrayValidator::class])
annotation class SizeCharArray(
    val min: Int = 0,
    val max: Int = Int.MAX_VALUE,
    val message: String = "Password must be between {min} and {max} characters",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class SizeCharArrayValidator : ConstraintValidator<SizeCharArray, CharArray?> {
    private var min: Int = 0
    private var max: Int = Int.MAX_VALUE

    override fun initialize(constraintAnnotation: SizeCharArray) {
        min = constraintAnnotation.min
        max = constraintAnnotation.max
    }

    override fun isValid(
        value: CharArray?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (value == null) {
            return true // null values are handled by @NotBlankCharArray
        }

        val length = value.size
        return length >= min && length <= max
    }
}
