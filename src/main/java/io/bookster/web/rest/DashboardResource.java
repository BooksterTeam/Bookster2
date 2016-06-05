package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.BooksterUser;
import io.bookster.domain.Lending;
import io.bookster.domain.LendingRequest;
import io.bookster.domain.User;
import io.bookster.service.*;
import io.bookster.web.rest.dto.DashboardDTO;
import io.bookster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LendingRequest.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private LendingRequestService lendingRequestService;

    @Inject
    private CopyService copyService;

    @Inject
    private LendingService lendingService;

    @Inject
    private UserService userService;

    @Inject
    private BooksterUserService booksterUserService;

    @Inject
    private MailService mailService;

    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dashboard",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DashboardDTO> getDashboard() throws URISyntaxException {

        User user = userService.getUserWithAuthorities();
        Optional<BooksterUser> requestFrom = booksterUserService.findByUser(user);
        if (!requestFrom.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlertMessage("booksterUser", "The current logged in user does not have a booksterUser")).body(null);
        }

        BooksterUser booksterUser = requestFrom.get();
        List<LendingRequest> lendingRequests = lendingRequestService.getAllLendingRequestsByUser(booksterUser);
        List<Lending> lendings = lendingService.getAllLendingsByUser(booksterUser);

        return new ResponseEntity<>(new DashboardDTO(lendingRequests, lendings), null, HttpStatus.OK);
    }


}
