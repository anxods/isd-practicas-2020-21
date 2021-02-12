-- ----------------------------------------------------------------------------
-- Run Model
-------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE Inscription;
DROP TABLE Run;

-- --------------------------------- Run ------------------------------------
CREATE TABLE Run (
    runId BIGINT NOT NULL AUTO_INCREMENT,
    runLocation VARCHAR(255) COLLATE latin1_bin NOT NULL,
    runDescription VARCHAR(255) COLLATE latin1_bin NOT NULL,
    runStartDate DATETIME NOT NULL,
    runPrice FLOAT NOT NULL,
    runMaxParticipants INT NOT NULL,
    runParticipants INT NOT NULL,
    runCreationDate DATETIME NOT NULL,
    CONSTRAINT RunPK PRIMARY KEY(runId),
    CONSTRAINT validRunParticipants CHECK (runParticipants >= 0),
    CONSTRAINT validRunMaxParticipants CHECK (runMaxParticipants >= 0),
    CONSTRAINT validRunPrice CHECK (runPrice >= 0)
) ENGINE = InnoDB;

-- ----------------------------- Inscription ---------------------------------
CREATE TABLE Inscription (
    inscriptionId BIGINT NOT NULL AUTO_INCREMENT,
    runId BIGINT NOT NULL,
    inscriptionDorsal VARCHAR(255) COLLATE latin1_bin NOT NULL,
    inscriptionUserEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    inscriptionCreditCardNumber VARCHAR(255) COLLATE latin1_bin NOT NULL,
    inscriptionDate DATETIME NOT NULL,
    isDorsalTaken BIT NOT NULL,
    CONSTRAINT InscriptionPK PRIMARY KEY(inscriptionId, runId),
    CONSTRAINT InscriptionRunIdFK FOREIGN KEY(runId) REFERENCES Run(runId) ON DELETE CASCADE
) ENGINE = InnoDB;