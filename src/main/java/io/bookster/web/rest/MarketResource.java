package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.BooksterUser;
import io.bookster.domain.Copy;
import io.bookster.domain.LendingRequest;
import io.bookster.domain.User;
import io.bookster.domain.enumeration.RequestStatus;
import io.bookster.service.*;
import io.bookster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * REST controller for managing LendingRequest.
 */
@RestController
@RequestMapping("/api")
public class MarketResource {

    private final Logger log = LoggerFactory.getLogger(MarketResource.class);

    @Inject
    private LendingRequestService lendingRequestService;

    @Inject
    private CopyService copyService;

    @Inject
    private UserService userService;

    @Inject
    private BooksterUserService booksterUserService;

    @Inject
    private MailService mailService;

    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @param lendingRequest the lendingRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/market/borrow",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity createLendingRequest(@RequestBody LendingRequest lendingRequest) throws URISyntaxException {
        log.debug("REST request to save LendingRequest : {}", lendingRequest);
        if (lendingRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lendingRequest", "idexists", "A new lendingRequest cannot already have an ID")).body(null);
        }
        Date input = new Date();
        LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lendingRequest.setCreatedDate(date);

        User user = userService.getUserWithAuthorities();
        Optional<BooksterUser> requestFrom = booksterUserService.findByUser(user);
        if (!requestFrom.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("booksterUser", "notexists", "The current logged in user does not have a booksterUser")).body(null);
        }

        Copy copy = lendingRequest.getCopie();
        if (!copy.isAvailable()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("copy", "notavailable", "The copy is currently not available")).body(null);
        }

        copy.setAvailable(false);
        copyService.save(copy);
        log.info("Copy:{}", copy);
        //TODO sent Email
        BooksterUser owner = copy.getBooksterUser();

        lendingRequest.setBooksterUser(requestFrom.get());
        lendingRequest.setCopie(copy);
        lendingRequest.setStatus(RequestStatus.PENDING);
        lendingRequestService.save(lendingRequest);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Successfully created the request", "Success")).body(lendingRequest);
    }


}
