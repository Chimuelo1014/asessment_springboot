ALTER TABLE credit_applications
    ADD CONSTRAINT fk_credit_app_affiliate
    FOREIGN KEY (affiliate_id)
    REFERENCES affiliates(id);

ALTER TABLE risk_evaluations
    ADD CONSTRAINT fk_risk_eval_credit_app
    FOREIGN KEY (credit_application_id)
    REFERENCES credit_applications(id);
