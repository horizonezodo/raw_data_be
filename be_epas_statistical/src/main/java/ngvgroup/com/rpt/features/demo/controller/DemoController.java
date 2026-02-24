package ngvgroup.com.rpt.features.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.logging.activity.annotation.NoActivityLog;

@RestController
@RequestMapping("/demo")
//@LogActivity(function = "Nhật ký kiểm tra")
public class DemoController {


    @LogActivity(function = "Demo test 1")
    @GetMapping("test1")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok("log test 1");
    }

    @LogActivity(function = "Demo test 2")
    @GetMapping("test2")
    @NoActivityLog
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok("log test 2");
    }

    @GetMapping("test3")
    @LogActivity(action = "đang test 3"  )
    public ResponseEntity<String> test3() {
        return ResponseEntity.ok("log test 3");
    }
}
