package ngvgroup.com.rpt.features.dim.service;

import ngvgroup.com.rpt.features.dim.model.DimProvD;

import java.util.List;

public interface DimProvDService {
    List<DimProvD> getAll(String ciId);
}
