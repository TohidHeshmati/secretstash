package com.tohid.secretstash.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotBlankCharArrayValidator::class])
annotation class NotBlankCharArray(
    val message: String = "Password is required",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class NotBlankCharArrayValidator : ConstraintValidator<NotBlankCharArray, CharArray?> {
    override fun isValid(
        value: CharArray?,
        context: ConstraintValidatorContext,
    ): Boolean = value != null && value.isNotEmpty()
}
