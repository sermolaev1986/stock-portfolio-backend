package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.PositionDTO;
import io.stock.portfolio.backend.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/positions", produces = {"application/json"})
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/username/{owner}")
    @ResponseStatus(HttpStatus.OK)
    public List<PositionDTO> getProfileByUsername(@PathVariable String owner) {

        return positionService.getAllPositionsByOwner(owner);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<PositionDTO> getProfiles() {

        return positionService.getAllPositions();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void postProfiles(@RequestBody @Valid PositionDTO positionDTO) {

        positionService.postPosition(positionDTO);
    }

}
