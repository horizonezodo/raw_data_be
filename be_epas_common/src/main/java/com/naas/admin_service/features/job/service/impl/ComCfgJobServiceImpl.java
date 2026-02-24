package com.naas.admin_service.features.job.service.impl;

import com.naas.admin_service.features.job.dto.ComCfgJobDto;
import com.naas.admin_service.features.job.dto.CreateJobRequest;
import com.naas.admin_service.features.job.dto.UpdateJobRequest;
import com.naas.admin_service.features.job.model.ComCfgJob;
import com.naas.admin_service.features.job.repository.ComCfgJobRepository;
import com.naas.admin_service.features.job.repository.ComLogJobRepository;
import com.naas.admin_service.features.job.service.ComCfgJobService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComCfgJobServiceImpl implements ComCfgJobService {

    private final ScheduleJob scheduleJob;
    private final CancelJob cancelJob;
    private final ComCfgJobRepository cfgJobRepo;
    private final ComLogJobRepository logJobRepo;

    // ✅ init bootstrap đã chuyển sang:
    // - SingleTenantJobBootstrap (enabled=false)
    // - MultiTenantJobBootstrap (enabled=true)

    @Transactional
    @Override
    public ComCfgJob createAndStartJob(CreateJobRequest createJobRequest) {
        ComCfgJob job = new ComCfgJob();
        job.setJobName(createJobRequest.getJobName());
        job.setCronExpr(createJobRequest.getCronExpr());
        job.setIsActive(1);
        job.setDescription(createJobRequest.getDescription());

        job = cfgJobRepo.save(job);

        scheduleJob.scheduleJobInternal(job);
        return job;
    }

    @Transactional
    @Override
    public void startJob(Long jobId) {
        ComCfgJob job = cfgJobRepo.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, jobId));

        job.setIsActive(1);
        cfgJobRepo.save(job);

        scheduleJob.scheduleJobInternal(job);
    }

    @Transactional
    @Override
    public void stopJob(Long jobId) {
        cancelJob.cancelJobSchedule(jobId);

        cfgJobRepo.findById(jobId).ifPresent(job -> {
            job.setIsActive(0);
            cfgJobRepo.save(job);
        });
    }

    @Transactional
    @Override
    public void updateJobCron(Long jobId, UpdateJobRequest req) {
        ComCfgJob job = cfgJobRepo.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, jobId));

        job.setCronExpr(req.getCronExpr());
        job.setDescription(req.getDescription());
        cfgJobRepo.save(job);

        if (job.getIsActive() != null && job.getIsActive() == 1) {
            scheduleJob.scheduleJobInternal(job);
        }
    }

    @Transactional
    @Override
    public void deleteJob(Long jobId, boolean deleteLogs) {
        cancelJob.cancelJobSchedule(jobId);

        if (deleteLogs) {
            int deletedLogs = logJobRepo.deleteByJobId(jobId);
            log.info("Deleted {} log records in COM_LOG_JOB for jobId={}", deletedLogs, jobId);
        }

        if (cfgJobRepo.existsById(jobId)) {
            cfgJobRepo.deleteById(jobId);
            log.info("Deleted job config COM_CFG_JOB id={}", jobId);
        } else {
            log.warn("Job id={} not found when deleting", jobId);
        }
    }

    @Override
    public Page<ComCfgJobDto> search(String search, Pageable pageable) {
        return cfgJobRepo.search(search, pageable);
    }
}
