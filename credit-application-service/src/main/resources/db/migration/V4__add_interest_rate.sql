
-- Agregar campo interest_rate (tasa de interés) según enunciado
ALTER TABLE credit_applications
ADD COLUMN interest_rate DECIMAL(5, 2);

-- Actualizar aplicaciones existentes con una tasa por defecto (12.5%)
UPDATE credit_applications
SET interest_rate = 12.5
WHERE interest_rate IS NULL;

-- Hacer el campo NOT NULL después de la actualización
ALTER TABLE credit_applications
ALTER COLUMN interest_rate SET NOT NULL;

-- Agregar constraint para validar que la tasa sea positiva
ALTER TABLE credit_applications
ADD CONSTRAINT chk_interest_rate_positive CHECK (interest_rate >= 0);

-- Comentario para documentación
COMMENT ON COLUMN credit_applications.interest_rate IS 'Tasa de interés anual propuesta en porcentaje (ej: 12.5 = 12.5%)';