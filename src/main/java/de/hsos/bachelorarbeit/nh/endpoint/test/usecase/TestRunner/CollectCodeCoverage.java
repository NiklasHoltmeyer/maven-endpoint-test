package de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;

import java.util.List;

public abstract class CollectCodeCoverage {
    public abstract List<CodeCoverageResult> collectCodeCoverage() throws Exception;
    public UnitTestGroupedResult combine(UnitTestGroupedResult unitTestGroupedResult, List<CodeCoverageResult> codeCoverageResults){
        codeCoverageResults.stream()
                .forEach(unitTestGroupedResult::addCoverage);
        return unitTestGroupedResult;
    }
}
