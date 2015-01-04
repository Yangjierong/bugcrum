/*
 * $Id: SprintPeriodNotConflictValidator.java 81 2014-12-18 10:37:49Z jierong $
 * 
 * Copyright (c) 2001-2008 Accentrix Company Limited. All Rights Reserved.
 * 
 * Accentrix Company Limited. ("Accentrix") retains copyright on all text, source
 * and binary code contained in this software and documentation. Accentrix grants
 * Licensee a limited license to use this software, provided that this copyright
 * notice and license appear on all copies of the software. The software source
 * code is provided for reference purposes only and may not be copied, modified 
 * or distributed.
 * 
 * THIS SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS," WITHOUT ANY WARRANTY OF
 * ANY KIND UNLESS A SEPARATE WARRANTIES IS PURCHASED FROM ACCENTRIX AND REMAINS
 * VALID.  ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ACCENTRIX SHALL NOT BE LIABLE
 * FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING OR MODIFYING THE
 * SOFTWARE OR ITS DERIVATIVES.
 * 
 * IN NO EVENT WILL ACCENTRIX BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF ACCENTRIX HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 */
package com.accentrix.bugcrum.validation.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.accentrix.bugcrum.domain.scrum.Sprint;
import com.accentrix.bugcrum.service.SprintService;
import com.accentrix.bugcrum.validation.SprintPeriodNotConflict;
import com.accentrix.nttca.dcms.common.cache.CacheScope;
import com.accentrix.nttca.dcms.common.cache.annotation.Cacheable;

@Configurable
public class SprintPeriodNotConflictValidator implements
        ConstraintValidator<SprintPeriodNotConflict, Sprint> {
    private static final Logger log = LoggerFactory
            .getLogger(SprintPeriodNotConflictValidator.class);

    @Autowired
    private SprintService sprintService;

    @Override
    public void initialize(SprintPeriodNotConflict constraintAnnotation) {
    }

    @Override
    public boolean isValid(Sprint sprint, ConstraintValidatorContext context) {

        return doValidation(sprint);
    }

    //@Cacheable(group = "validator", cacheScope = CacheScope.thread)
    private boolean doValidation(Sprint sprint) {
        if (sprint == null) {
            return true;
        }

        log.info("isValiding {}", sprint.getName());

        if (sprint.getStartDate() == null) {
            return true;
        }

        if (sprint.getEndDate() == null) {
            return true;
        }

        List<Sprint> existSprints = sprintService.findByProductId(sprint.getProduct().getId());
        for (Sprint other : existSprints) {
            if (sprint.getId() != null && sprint.getId().intValue() == other.getId().intValue()) {
                continue;
            }

            if (sprint.getStartDate().before(other.getStartDate())
                    && sprint.getEndDate().after(other.getStartDate())) {
                return false;
            }

            if (sprint.getStartDate().after(other.getStartDate())
                    && sprint.getStartDate().before(other.getEndDate())) {
                return false;
            }
        }

        return true;
    }

}
