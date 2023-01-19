package bibliomar.bibliomarserver.controller.metadata;

import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.service.metadata.MetadataService;
import bibliomar.bibliomarserver.utils.Topics;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @GetMapping("/{topic}/{md5}")
    public ResponseEntity<Metadata> getMetadata(@PathVariable Topics topic, @PathVariable String md5) {
        if (topic == Topics.fiction) {
            return ResponseEntity.ok(this.metadataService.getFictionMetadata(md5));
        } else if (topic == Topics.scitech) {
            return ResponseEntity.ok(null);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic");
        }
    }
}
