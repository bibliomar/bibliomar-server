package bibliomar.bibliomarserver.controllers;

import bibliomar.bibliomarserver.models.metadata.Metadata;
import bibliomar.bibliomarserver.services.MetadataService;
import bibliomar.bibliomarserver.utils.contants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @GetMapping("/{topic}/{md5}")
    public ResponseEntity<Metadata> getMetadata(@PathVariable Topics topic, @PathVariable String md5)
            throws ExecutionException, InterruptedException {
        if (topic == Topics.fiction) {
            return ResponseEntity.ok(this.metadataService.getFictionMetadata(md5).get());
        } else if (topic == Topics.scitech) {
            return ResponseEntity.ok(this.metadataService.getScitechMetadata(md5).get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic");
        }
    }
}
