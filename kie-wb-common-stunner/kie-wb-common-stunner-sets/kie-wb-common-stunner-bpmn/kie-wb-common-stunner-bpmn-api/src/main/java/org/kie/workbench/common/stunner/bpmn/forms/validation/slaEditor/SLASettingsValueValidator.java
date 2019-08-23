/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.forms.validation.slaEditor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.gwt.regexp.shared.RegExp;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.SLADueDate;
import org.kie.workbench.common.stunner.bpmn.forms.validation.timerEditor.TimerSettingsValueValidator;

public class SLASettingsValueValidator
        implements ConstraintValidator<ValidSLASettingsValue, SLADueDate> {

    public static final String TIME_DURATION_INVALID = "The SLA Due Date must be a valid ISO-8601 duration or an expression like #{expression}.";

    /**
     * ISO8601-Duration
     */
    private static final String ISO_DURATION = "P(?:\\d+(?:\\.\\d+)?D)?(?:T(?:\\d+(?:\\.\\d+)?H)?(?:\\d+(?:\\.\\d+)?M)?(?:\\d+(?:\\.\\d+)?S)?)?";

    public static final RegExp durationExpr = RegExp.compile("^" + ISO_DURATION + "$");

    public void initialize(ValidSLASettingsValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(SLADueDate slaDueDate,
                           ConstraintValidatorContext constraintValidatorContext) {

        if (slaDueDate != null && TimerSettingsValueValidator.hasSomething(slaDueDate.getValue())) {
            String value = slaDueDate.getValue();
            final boolean looksLikeExpression = TimerSettingsValueValidator.looksLikeExpression(value);

            if ((looksLikeExpression && !TimerSettingsValueValidator.isValidExpression(value)) ||
                    (!looksLikeExpression && !isValidDuration(value))) {

                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(TIME_DURATION_INVALID)
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    public static boolean isValidDuration(final String value) {
        return TimerSettingsValueValidator.hasSomething(value) && durationExpr.test(value);
    }
}