package fr.bt.backend.web.rest

import fr.bt.backend.service.AuditEventService

import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

import java.time.LocalDate
import java.time.ZoneId

/**
 * REST controller for getting the `AuditEvent`s.
 */
@RestController
@RequestMapping("/management/audits")
class AuditResource(private val auditEventService: AuditEventService) {

    /**
     * `GET /audits` : get a page of `AuditEvent`s.
     *
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @param pageable the pagination information.
     * @return the `ResponseEntity` with status `200 (OK)` and the list of `AuditEvent`s in body.
     */
    @GetMapping
    fun getAll(@RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder, pageable: Pageable): ResponseEntity<List<AuditEvent>> {
        val page = auditEventService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity(page.content, headers, HttpStatus.OK)
    }

    /**
     * `GET  /audits` : get a page of `AuditEvent`s between the `fromDate` and `toDate`.
     *
     * @param fromDate the start of the time period of `AuditEvent`s to get.
     * @param toDate the end of the time period of `AuditEvent`s to get.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @param pageable the pagination information.
     * @return the `ResponseEntity` with status `200 (OK)` and the list of `AuditEvent`s in body.
     */
    @GetMapping(params = ["fromDate", "toDate"])
    fun getByDates(
        @RequestParam(value = "fromDate") fromDate: LocalDate,
        @RequestParam(value = "toDate") toDate: LocalDate,
        @RequestParam queryParams: MultiValueMap<String, String>,
        uriBuilder: UriComponentsBuilder,
        pageable: Pageable
    ): ResponseEntity<List<AuditEvent>> {

        val page = auditEventService.findByDates(
            fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(),
            pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity(page.content, headers, HttpStatus.OK)
    }

    /**
     * `GET  /audits/:id` : get an `AuditEvent` by id.
     *
     * @param id the id of the entity to get.
     * @return the `ResponseEntity` with status `200 (OK)` and the AuditEvent in body, or status `404 (Not Found)`.
     */
    @GetMapping("/{id:.+}")
    fun get(@PathVariable id: Long?): ResponseEntity<AuditEvent> {
        return ResponseUtil.wrapOrNotFound(auditEventService.find(id!!))
    }
}
