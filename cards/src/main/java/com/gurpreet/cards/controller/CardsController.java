package com.gurpreet.cards.controller;

import com.gurpreet.cards.constants.CardsConstants;
import com.gurpreet.cards.dto.CardsContactInfoDto;
import com.gurpreet.cards.dto.CardsDto;
import com.gurpreet.cards.dto.ErrorResponseDto;
import com.gurpreet.cards.dto.ResponseDto;
import com.gurpreet.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Tag(
        name = "CRUD REST API for Cards in a Bank",
        description = "CRUD REST API in a Bank to CREATE, UPDATE, FETCH AND DELETE Card Details"
)
@RestController
@RequestMapping(path ="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardsController {

    private final ICardsService iCardsService;

    public CardsController(ICardsService iCardsService){
        this.iCardsService = iCardsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card inside a Bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
        }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber){
        iCardsService.createCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    /**
     * Fetches the card details associated with the given mobile number.
     *
     * This method takes a mobile number as a parameter and fetches the associated card details.
     * If no card is associated with the given mobile number, the method throws a ResourceNotFoundException.
     *
     * @param mobileNumber the mobile number of the customer whose card is to be retrieved
     * @return a CardsDto object containing the card details
     * @throws ResourceNotFoundException if no card is associated with the given mobile number
     */
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        CardsDto cardsDto = iCardsService.fetchCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    /**
     * Updates the card details associated with the given card number.
     *
     * This method takes a CardsDto object as a parameter and updates the associated card details.
     * If no card is associated with the given card number, the method throws a ResourceNotFoundException.
     *
     * @param cardsDto the object containing the updated card details
     * @return a ResponseDto object containing the status code and message
     * @throws ResourceNotFoundException if no card is associated with the given card number
     */
    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update card details based on a card number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
        boolean isUpdated = iCardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    /**
     * Deletes the card details associated with the given mobile number.
     *
     * This method takes a mobile number as a parameter and deletes the associated card details.
     * If no card is associated with the given mobile number, the method returns a 417 status code with a message indicating that the expectation failed.
     *
     * @param mobileNumber the mobile number of the customer whose card is to be deleted
     * @return a ResponseDto object containing the status code and message
     * @throws ResourceNotFoundException if no card is associated with the given mobile number
     */
    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }
    @Operation(
            summary = "Build Info REST API",
            description = "REST API to get the build version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){

        return ResponseEntity.
                status(HttpStatus.OK).
                body(buildVersion);
    }

    /**
     * @return The java version of the application
     */
    @Operation(
            summary = "Java Version REST API",
            description = "REST API to get the java version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){

        return ResponseEntity.
                status(HttpStatus.OK).
                body(environment.getProperty("JAVA_HOME"));
    }


    /**
    * Fetches the contact information of the application.
    *
    * This method provides the contact information of the application,
    *  including message, contact details, and on-call support.
    *
    * @return a ResponseEntity containing an AccountContactInfoDto with the contact information
    * @throws ErrorResponseDto if an internal server error occurs
    */
    @Operation(
            summary = "Contact Info REST API",
            description = "REST API to get the contact info of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo(){

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cardsContactInfoDto);
    }
}
