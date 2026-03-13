package com.pragma.bootcamps.capability.domain.usecases;

import com.pragma.bootcamps.capability.domain.clients.TechnologyAssociationClientPort;
import com.pragma.bootcamps.capability.domain.exceptions.InvalidCountException;
import com.pragma.bootcamps.capability.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.capability.domain.exceptions.RepeatedCapabilitiesException;
import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.spi.BootcampCapabilityPersistencePort;
import com.pragma.bootcamps.capability.domain.spi.CapabilityPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampCapabilityUseCaseTest {

    @Mock
    private BootcampCapabilityPersistencePort bootcampCapabilityPersistencePort;

    @Mock
    private CapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private TechnologyAssociationClientPort technologyAssociationClientPort;

    @InjectMocks
    private BootcampCapabilityUseCase bootcampCapabilityUseCase;

    private static final Long BOOTCAMP_ID = 1L;

    @Nested
    @DisplayName("associateCapabilities")
    class AssociateCapabilitiesTests {

        @Test
        @DisplayName("Should associate capabilities successfully when input is valid")
        void shouldAssociateCapabilitiesSuccessfully() {
            List<Long> capabilityIds = List.of(1L, 2L, 3L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.just(3L));
            when(bootcampCapabilityPersistencePort.saveAll(BOOTCAMP_ID, capabilityIds)).thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .verifyComplete();

            verify(capabilityPersistencePort).countByIds(capabilityIds);
            verify(bootcampCapabilityPersistencePort).saveAll(BOOTCAMP_ID, capabilityIds);
        }

        @Test
        @DisplayName("Should associate capabilities when exactly MIN_CAPS (1) capability is provided")
        void shouldAssociateWhenMinCapabilities() {
            List<Long> capabilityIds = List.of(1L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.just(1L));
            when(bootcampCapabilityPersistencePort.saveAll(BOOTCAMP_ID, capabilityIds)).thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .verifyComplete();

            verify(bootcampCapabilityPersistencePort).saveAll(BOOTCAMP_ID, capabilityIds);
        }

        @Test
        @DisplayName("Should associate capabilities when exactly MAX_CAPS (4) capabilities are provided")
        void shouldAssociateWhenMaxCapabilities() {
            List<Long> capabilityIds = List.of(1L, 2L, 3L, 4L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.just(4L));
            when(bootcampCapabilityPersistencePort.saveAll(BOOTCAMP_ID, capabilityIds)).thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .verifyComplete();

            verify(bootcampCapabilityPersistencePort).saveAll(BOOTCAMP_ID, capabilityIds);
        }

        @Test
        @DisplayName("Should throw InvalidCountException when capability list is empty")
        void shouldThrowInvalidCountWhenEmptyList() {
            List<Long> capabilityIds = Collections.emptyList();

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(InvalidCountException.class)
                    .verify();

            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw InvalidCountException when capability list exceeds MAX_CAPS (4)")
        void shouldThrowInvalidCountWhenTooManyCapabilities() {
            List<Long> capabilityIds = List.of(1L, 2L, 3L, 4L, 5L);

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(InvalidCountException.class)
                    .verify();

            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw RepeatedCapabilitiesException when there are duplicate IDs")
        void shouldThrowRepeatedCapabilitiesException() {
            List<Long> capabilityIds = Arrays.asList(1L, 2L, 2L);

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(RepeatedCapabilitiesException.class)
                    .verify();

            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw NotFoundException when some capability IDs do not exist")
        void shouldThrowNotFoundWhenCapabilityIdsDoNotExist() {
            List<Long> capabilityIds = List.of(1L, 2L, 3L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.just(2L));

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(NotFoundException.class)
                    .verify();

            verify(capabilityPersistencePort).countByIds(capabilityIds);
            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw NotFoundException when no capability IDs exist in DB")
        void shouldThrowNotFoundWhenNoCapabilityIdsExist() {
            List<Long> capabilityIds = List.of(99L, 100L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.just(0L));

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(NotFoundException.class)
                    .verify();

            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw NotFoundException when countByIds returns empty Mono")
        void shouldThrowNotFoundWhenCountByIdsReturnsEmpty() {
            List<Long> capabilityIds = List.of(1L, 2L);

            when(capabilityPersistencePort.countByIds(capabilityIds)).thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.associateCapabilities(BOOTCAMP_ID, capabilityIds))
                    .expectError(NotFoundException.class)
                    .verify();

            verify(bootcampCapabilityPersistencePort, never()).saveAll(anyLong(), anyList());
        }
    }

    // ==================== getCapabilitiesByBootcampId ====================

    @Nested
    @DisplayName("getCapabilitiesByBootcampId")
    class GetCapabilitiesByBootcampIdTests {

        @Test
        @DisplayName("Should return capabilities associated with the bootcamp")
        void shouldReturnCapabilities() {
            Capability cap1 = Capability.builder().id(1L).name("Java").description("Java cap").build();
            Capability cap2 = Capability.builder().id(2L).name("Spring").description("Spring cap").build();

            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(1L, 2L));
            when(capabilityPersistencePort.findCapabilityById(1L)).thenReturn(Mono.just(cap1));
            when(capabilityPersistencePort.findCapabilityById(2L)).thenReturn(Mono.just(cap2));

            StepVerifier.create(bootcampCapabilityUseCase.getCapabilitiesByBootcampId(BOOTCAMP_ID))
                    .expectNext(cap1)
                    .expectNext(cap2)
                    .verifyComplete();

            verify(bootcampCapabilityPersistencePort).findCapabilityIdsByBootcampId(BOOTCAMP_ID);
            verify(capabilityPersistencePort).findCapabilityById(1L);
            verify(capabilityPersistencePort).findCapabilityById(2L);
        }

        @Test
        @DisplayName("Should return empty Flux when no capabilities are associated")
        void shouldReturnEmptyWhenNoCapabilities() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.empty());

            StepVerifier.create(bootcampCapabilityUseCase.getCapabilitiesByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(bootcampCapabilityPersistencePort).findCapabilityIdsByBootcampId(BOOTCAMP_ID);
            verify(capabilityPersistencePort, never()).findCapabilityById(anyLong());
        }

        @Test
        @DisplayName("Should return single capability when only one is associated")
        void shouldReturnSingleCapability() {
            Capability cap = Capability.builder().id(1L).name("React").description("React cap").build();

            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(1L));
            when(capabilityPersistencePort.findCapabilityById(1L)).thenReturn(Mono.just(cap));

            StepVerifier.create(bootcampCapabilityUseCase.getCapabilitiesByBootcampId(BOOTCAMP_ID))
                    .expectNext(cap)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("deleteAssociatedDataByBootcampId")
    class DeleteAssociatedDataByBootcampIdTests {

        @Test
        @DisplayName("Should cascade delete orphan capabilities and their technologies")
        void shouldCascadeDeleteOrphanCapabilities() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(1L, 2L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(1L))
                    .thenReturn(Mono.just(1L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(2L))
                    .thenReturn(Mono.just(1L));
            when(technologyAssociationClientPort.deleteTechnologiesByCapabilityIds(List.of(1L, 2L)))
                    .thenReturn(Mono.empty());
            when(capabilityPersistencePort.deleteCapabilitiesByIds(List.of(1L, 2L)))
                    .thenReturn(Mono.empty());
            when(bootcampCapabilityPersistencePort.deleteAssociationsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.deleteAssociatedDataByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(technologyAssociationClientPort).deleteTechnologiesByCapabilityIds(List.of(1L, 2L));
            verify(capabilityPersistencePort).deleteCapabilitiesByIds(List.of(1L, 2L));
            verify(bootcampCapabilityPersistencePort).deleteAssociationsByBootcampId(BOOTCAMP_ID);
        }

        @Test
        @DisplayName("Should not cascade delete when capabilities are used by multiple bootcamps")
        void shouldNotCascadeDeleteNonOrphanCapabilities() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(1L, 2L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(1L))
                    .thenReturn(Mono.just(3L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(2L))
                    .thenReturn(Mono.just(2L));
            when(bootcampCapabilityPersistencePort.deleteAssociationsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.deleteAssociatedDataByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(technologyAssociationClientPort, never()).deleteTechnologiesByCapabilityIds(anyList());
            verify(capabilityPersistencePort, never()).deleteCapabilitiesByIds(anyList());
            verify(bootcampCapabilityPersistencePort).deleteAssociationsByBootcampId(BOOTCAMP_ID);
        }

        @Test
        @DisplayName("Should cascade delete only orphan capabilities (mixed scenario)")
        void shouldCascadeDeleteOnlyOrphanCapabilities() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(1L, 2L, 3L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(1L))
                    .thenReturn(Mono.just(1L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(2L))
                    .thenReturn(Mono.just(3L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(3L))
                    .thenReturn(Mono.just(1L));
            when(technologyAssociationClientPort.deleteTechnologiesByCapabilityIds(anyList()))
                    .thenReturn(Mono.empty());
            when(capabilityPersistencePort.deleteCapabilitiesByIds(anyList()))
                    .thenReturn(Mono.empty());
            when(bootcampCapabilityPersistencePort.deleteAssociationsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.deleteAssociatedDataByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(technologyAssociationClientPort).deleteTechnologiesByCapabilityIds(anyList());
            verify(capabilityPersistencePort).deleteCapabilitiesByIds(anyList());
            verify(bootcampCapabilityPersistencePort).deleteAssociationsByBootcampId(BOOTCAMP_ID);
        }

        @Test
        @DisplayName("Should only delete associations when bootcamp has no associated capabilities")
        void shouldHandleNoAssociatedCapabilities() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.empty());
            when(bootcampCapabilityPersistencePort.deleteAssociationsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.deleteAssociatedDataByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(technologyAssociationClientPort, never()).deleteTechnologiesByCapabilityIds(anyList());
            verify(capabilityPersistencePort, never()).deleteCapabilitiesByIds(anyList());
            verify(bootcampCapabilityPersistencePort).deleteAssociationsByBootcampId(BOOTCAMP_ID);
        }

        @Test
        @DisplayName("Should cascade delete single orphan capability")
        void shouldCascadeDeleteSingleOrphanCapability() {
            when(bootcampCapabilityPersistencePort.findCapabilityIdsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Flux.just(5L));
            when(bootcampCapabilityPersistencePort.countBootcampsByCapability(5L))
                    .thenReturn(Mono.just(1L));
            when(technologyAssociationClientPort.deleteTechnologiesByCapabilityIds(List.of(5L)))
                    .thenReturn(Mono.empty());
            when(capabilityPersistencePort.deleteCapabilitiesByIds(List.of(5L)))
                    .thenReturn(Mono.empty());
            when(bootcampCapabilityPersistencePort.deleteAssociationsByBootcampId(BOOTCAMP_ID))
                    .thenReturn(Mono.empty());

            StepVerifier.create(bootcampCapabilityUseCase.deleteAssociatedDataByBootcampId(BOOTCAMP_ID))
                    .verifyComplete();

            verify(technologyAssociationClientPort).deleteTechnologiesByCapabilityIds(List.of(5L));
            verify(capabilityPersistencePort).deleteCapabilitiesByIds(List.of(5L));
            verify(bootcampCapabilityPersistencePort).deleteAssociationsByBootcampId(BOOTCAMP_ID);
        }
    }
}
