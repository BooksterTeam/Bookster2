package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.*;
import io.bookster.domain.enumeration.RequestStatus;
import io.bookster.service.*;
import io.bookster.web.rest.dto.CopyDashDTO;
import io.bookster.web.rest.dto.DashboardDTO;
import io.bookster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<LendingRequest> externLendingRequests = lendingRequestService.getAllExternLendingRequestByUser(booksterUser);
        List<LendingRequest> lendingRequests = lendingRequestService.getAllLendingRequestsByUser(booksterUser);
        List<Lending> lendings = lendingService.getAllLendingsByUser(booksterUser);

        List<Copy> copies = copyService.getAllCopiesByUser(booksterUser);

        List<CopyDashDTO> copiesDash = copies.stream().map(copy -> {

            if (copy.isAvailable()) {
                return new CopyDashDTO(copy.getId(), copy.getBook(), copy.isAvailable());
            }

            return new CopyDashDTO(copy.getId(), copy.getBook(),copy.isAvailable(), lendingRequestService.findByCopy(copy));
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new DashboardDTO(externLendingRequests, lendingRequests, lendings, copiesDash), null, HttpStatus.OK);
    }

    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dashboard/reject",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity rejectLendingRequest(@RequestBody LendingRequest lendingRequest) throws URISyntaxException {
        log.info("{}", lendingRequest);
        lendingRequest.setStatus(RequestStatus.REJECTED);
        Copy copy = lendingRequest.getCopie();
        copy.setAvailable(true);
        copyService.save(copy);
        lendingRequest.setCopie(null);
        lendingRequestService.save(lendingRequest);
        //TODO mail service send email
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Successfully rejected the request", "Success")).body(null);
    }

    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dashboard/accept",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity acceptLendingRequest(@RequestBody LendingRequest lendingRequest) throws URISyntaxException {
        log.info("{}", lendingRequest);
        BooksterUser booksterUser = lendingRequest.getBooksterUser();
        Lending lending = new Lending(booksterUser, lendingRequest.getFromDate(), lendingRequest.getDueDate(), lendingRequest.getCopie());
        lendingService.save(lending);
        lendingRequest.setStatus(RequestStatus.ACCEPTED);
        lendingRequestService.save(lendingRequest);

        //TODO mail service send email
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Successfully accepted the request", "Success")).body(null);
    }


    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dashboard/return",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity returnLending(@RequestBody Lending lending) throws URISyntaxException {
        log.info("{}", lending);
        Lending one = lendingService.findOne(lending.getId());
        Copy copy = one.getCopy();

        copy.setAvailable(true);
        copyService.save(copy);
        LendingRequest lendingRequest = lendingRequestService.findByCopy(copy);
        lendingRequestService.delete(lendingRequest.getId());

        lending.setCopy(null);
        lendingService.delete(lending.getId());


        /*
        BooksterUser booksterUser = lendingRequest.getBooksterUser();
        Lending lending = new Lending(booksterUser, lendingRequest.getFromDate(), lendingRequest.getDueDate(), lendingRequest.getCopie());
        lendingService.save(lending);
        lendingRequest.setStatus(RequestStatus.ACCEPTED);
        lendingRequestService.save(lendingRequest);

        //TODO mail service send email */
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Successfully accepted the request", "Success")).body(null);
    }

}
