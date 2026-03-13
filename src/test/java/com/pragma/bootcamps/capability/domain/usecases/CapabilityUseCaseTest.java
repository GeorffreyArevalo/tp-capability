package com.pragma.bootcamps.capability.domain.usecases;

import com.pragma.bootcamps.capability.domain.clients.TechnologyAssociationClientPort;
import com.pragma.bootcamps.capability.domain.exceptions.CapabilityAlreadyExistsException;
import com.pragma.bootcamps.capability.domain.exceptions.CapabilityTechnologiesCountException;
import com.pragma.bootcamps.capability.domain.exceptions.SagaCompensationException;
import com.pragma.bootcamps.capability.domain.models.Capability;
import com.pragma.bootcamps.capability.domain.models.CapabilityWithTechnologies;
import com.pragma.bootcamps.capability.domain.models.TechnologySummary;
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
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityUseCaseTest {

    @Mock
    private CapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private TechnologyAssociationClientPort technologyAssociationClientPort;

    @InjectMocks
    private CapabilityUseCase capabilityUseCase;


    @Nested
    @DisplayName("saveCapability")
    class SaveCapabilityTests {

        @Test
        @DisplayName("Should save capability and associate technologies successfully")
        void shouldSaveCapabilitySuccessfully() {
            List<Long> techIds = List.of(1L, 2L, 3L);
            Capability capability = Capability.builder()
                    .name("Backend")
                    .description("Backend capability")
                    .technologyIds(techIds)
                    .build();
            Capability savedCapability = capability.toBuilder().id(1L).build();

            when(capabilityPersistencePort.findCapabilityByName("Backend")).thenReturn(Mono.empty());
            when(capabilityPersistencePort.saveCapability(capability)).thenReturn(Mono.just(savedCapability));
            when(technologyAssociationClientPort.associateTechnologies(1L, techIds)).thenReturn(Mono.empty());

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectNext(savedCapability)
                    .verifyComplete();

            verify(capabilityPersistencePort).findCapabilityByName("Backend");
            verify(capabilityPersistencePort).saveCapability(capability);
            verify(technologyAssociationClientPort).associateTechnologies(1L, techIds);
        }

        @Test
        @DisplayName("Should save capability with exactly MIN_TECHS (3) technologies")
        void shouldSaveWithMinTechnologies() {
            List<Long> techIds = List.of(1L, 2L, 3L);
            Capability capability = Capability.builder()
                    .name("MinTech")
                    .description("Min tech capability")
                    .technologyIds(techIds)
                    .build();
            Capability savedCapability = capability.toBuilder().id(1L).build();

            when(capabilityPersistencePort.findCapabilityByName("MinTech")).thenReturn(Mono.empty());
            when(capabilityPersistencePort.saveCapability(capability)).thenReturn(Mono.just(savedCapability));
            when(technologyAssociationClientPort.associateTechnologies(1L, techIds)).thenReturn(Mono.empty());

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectNext(savedCapability)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should save capability with exactly MAX_TECHS (20) technologies")
        void shouldSaveWithMaxTechnologies() {
            List<Long> techIds = LongStream.rangeClosed(1, 20).boxed().toList();
            Capability capability = Capability.builder()
                    .name("MaxTech")
                    .description("Max tech capability")
                    .technologyIds(techIds)
                    .build();
            Capability savedCapability = capability.toBuilder().id(1L).build();

            when(capabilityPersistencePort.findCapabilityByName("MaxTech")).thenReturn(Mono.empty());
            when(capabilityPersistencePort.saveCapability(capability)).thenReturn(Mono.just(savedCapability));
            when(technologyAssociationClientPort.associateTechnologies(1L, techIds)).thenReturn(Mono.empty());

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectNext(savedCapability)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException when technologies count is below MIN_TECHS")
        void shouldThrowWhenTooFewTechnologies() {
            List<Long> techIds = List.of(1L, 2L);
            Capability capability = Capability.builder()
                    .name("FewTechs")
                    .description("Too few techs")
                    .technologyIds(techIds)
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
            verify(technologyAssociationClientPort, never()).associateTechnologies(anyLong(), anyList());
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException when technologies count exceeds MAX_TECHS")
        void shouldThrowWhenTooManyTechnologies() {
            List<Long> techIds = LongStream.rangeClosed(1, 21).boxed().toList();
            Capability capability = Capability.builder()
                    .name("ManyTechs")
                    .description("Too many techs")
                    .technologyIds(techIds)
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException when technologies list is empty")
        void shouldThrowWhenEmptyTechnologies() {
            Capability capability = Capability.builder()
                    .name("EmptyTechs")
                    .description("Empty techs")
                    .technologyIds(Collections.emptyList())
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException when technologies list is null")
        void shouldThrowWhenNullTechnologies() {
            Capability capability = Capability.builder()
                    .name("NullTechs")
                    .description("Null techs")
                    .technologyIds(null)
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException when technologies have duplicates")
        void shouldThrowWhenRepeatedTechnologies() {
            List<Long> techIds = Arrays.asList(1L, 2L, 2L, 3L);
            Capability capability = Capability.builder()
                    .name("RepeatTechs")
                    .description("Repeated techs")
                    .technologyIds(techIds)
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
        }

        @Test
        @DisplayName("Should throw CapabilityAlreadyExistsException when capability name already exists")
        void shouldThrowWhenCapabilityNameAlreadyExists() {
            List<Long> techIds = List.of(1L, 2L, 3L);
            Capability capability = Capability.builder()
                    .name("Existing")
                    .description("Already exists")
                    .technologyIds(techIds)
                    .build();
            Capability existingCapability = Capability.builder()
                    .id(99L)
                    .name("Existing")
                    .description("Existing description")
                    .build();

            when(capabilityPersistencePort.findCapabilityByName("Existing")).thenReturn(Mono.just(existingCapability));

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityAlreadyExistsException.class)
                    .verify();

            verify(capabilityPersistencePort).findCapabilityByName("Existing");
            verify(capabilityPersistencePort, never()).saveCapability(any());
        }

        @Test
        @DisplayName("Should perform saga compensation when technology association fails")
        void shouldCompensateWhenAssociationFails() {
            List<Long> techIds = List.of(1L, 2L, 3L);
            Capability capability = Capability.builder()
                    .name("SagaFail")
                    .description("Saga compensation test")
                    .technologyIds(techIds)
                    .build();
            Capability savedCapability = capability.toBuilder().id(10L).build();

            when(capabilityPersistencePort.findCapabilityByName("SagaFail")).thenReturn(Mono.empty());
            when(capabilityPersistencePort.saveCapability(capability)).thenReturn(Mono.just(savedCapability));
            when(technologyAssociationClientPort.associateTechnologies(10L, techIds))
                    .thenReturn(Mono.error(new RuntimeException("Association service unavailable")));
            when(capabilityPersistencePort.deleteCapability(10L)).thenReturn(Mono.empty());

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(SagaCompensationException.class)
                    .verify();

            verify(capabilityPersistencePort).saveCapability(capability);
            verify(technologyAssociationClientPort).associateTechnologies(10L, techIds);
            verify(capabilityPersistencePort).deleteCapability(10L);
        }

        @Test
        @DisplayName("Should throw CapabilityTechnologiesCountException with only 1 technology (below MIN)")
        void shouldThrowWhenOnlyOneTechnology() {
            List<Long> techIds = List.of(1L);
            Capability capability = Capability.builder()
                    .name("OneTech")
                    .description("Single tech")
                    .technologyIds(techIds)
                    .build();

            StepVerifier.create(capabilityUseCase.saveCapability(capability))
                    .expectError(CapabilityTechnologiesCountException.class)
                    .verify();

            verify(capabilityPersistencePort, never()).saveCapability(any());
        }
    }


    @Nested
    @DisplayName("getCapabilitiesWithTechnologies")
    class GetCapabilitiesWithTechnologiesTests {

        @Test
        @DisplayName("Should return capabilities with their associated technologies")
        void shouldReturnCapabilitiesWithTechnologies() {
            Capability cap1 = Capability.builder().id(1L).name("Backend").description("Backend cap").build();
            Capability cap2 = Capability.builder().id(2L).name("Frontend").description("Frontend cap").build();

            TechnologySummary tech1 = new TechnologySummary(1L, "Java");
            TechnologySummary tech2 = new TechnologySummary(2L, "Spring");
            TechnologySummary tech3 = new TechnologySummary(3L, "React");

            CapabilityWithTechnologies expected1 = CapabilityWithTechnologies.builder()
                    .id(1L).name("Backend").description("Backend cap")
                    .technologies(List.of(tech1, tech2))
                    .build();
            CapabilityWithTechnologies expected2 = CapabilityWithTechnologies.builder()
                    .id(2L).name("Frontend").description("Frontend cap")
                    .technologies(List.of(tech3))
                    .build();

            when(capabilityPersistencePort.findCapabilitiesPagedAndSorted(0, 10, "name", "asc"))
                    .thenReturn(Flux.just(cap1, cap2));
            when(technologyAssociationClientPort.getTechnologiesByCapabilityId(1L))
                    .thenReturn(Flux.just(tech1, tech2));
            when(technologyAssociationClientPort.getTechnologiesByCapabilityId(2L))
                    .thenReturn(Flux.just(tech3));

            StepVerifier.create(capabilityUseCase.getCapabilitiesWithTechnologies(0, 10, "name", "asc"))
                    .expectNext(expected1)
                    .expectNext(expected2)
                    .verifyComplete();

            verify(capabilityPersistencePort).findCapabilitiesPagedAndSorted(0, 10, "name", "asc");
            verify(technologyAssociationClientPort).getTechnologiesByCapabilityId(1L);
            verify(technologyAssociationClientPort).getTechnologiesByCapabilityId(2L);
        }

        @Test
        @DisplayName("Should return empty Flux when no capabilities exist")
        void shouldReturnEmptyWhenNoCapabilities() {
            when(capabilityPersistencePort.findCapabilitiesPagedAndSorted(0, 10, "name", "asc"))
                    .thenReturn(Flux.empty());

            StepVerifier.create(capabilityUseCase.getCapabilitiesWithTechnologies(0, 10, "name", "asc"))
                    .verifyComplete();

            verify(capabilityPersistencePort).findCapabilitiesPagedAndSorted(0, 10, "name", "asc");
            verify(technologyAssociationClientPort, never()).getTechnologiesByCapabilityId(anyLong());
        }

        @Test
        @DisplayName("Should return capability with empty technologies list when no technologies are associated")
        void shouldReturnCapabilityWithEmptyTechnologies() {
            Capability cap = Capability.builder().id(1L).name("EmptyTech").description("No techs").build();

            CapabilityWithTechnologies expected = CapabilityWithTechnologies.builder()
                    .id(1L).name("EmptyTech").description("No techs")
                    .technologies(Collections.emptyList())
                    .build();

            when(capabilityPersistencePort.findCapabilitiesPagedAndSorted(0, 5, "name", "desc"))
                    .thenReturn(Flux.just(cap));
            when(technologyAssociationClientPort.getTechnologiesByCapabilityId(1L))
                    .thenReturn(Flux.empty());

            StepVerifier.create(capabilityUseCase.getCapabilitiesWithTechnologies(0, 5, "name", "desc"))
                    .expectNext(expected)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should return single capability with technologies")
        void shouldReturnSingleCapabilityWithTechnologies() {
            Capability cap = Capability.builder().id(5L).name("DevOps").description("DevOps cap").build();
            TechnologySummary tech = new TechnologySummary(10L, "Docker");

            CapabilityWithTechnologies expected = CapabilityWithTechnologies.builder()
                    .id(5L).name("DevOps").description("DevOps cap")
                    .technologies(List.of(tech))
                    .build();

            when(capabilityPersistencePort.findCapabilitiesPagedAndSorted(1, 5, "id", "asc"))
                    .thenReturn(Flux.just(cap));
            when(technologyAssociationClientPort.getTechnologiesByCapabilityId(5L))
                    .thenReturn(Flux.just(tech));

            StepVerifier.create(capabilityUseCase.getCapabilitiesWithTechnologies(1, 5, "id", "asc"))
                    .expectNext(expected)
                    .verifyComplete();
        }
    }


    @Nested
    @DisplayName("isValidTechnologiesCount")
    class IsValidTechnologiesCountTests {

        @Test
        @DisplayName("Should return true when count is within valid range")
        void shouldReturnTrueWhenCountIsValid() {
            List<Long> techIds = List.of(1L, 2L, 3L, 4L, 5L);
            assertTrue(capabilityUseCase.isValidTechnologiesCount(techIds, 3, 20));
        }

        @Test
        @DisplayName("Should return true when count equals min boundary")
        void shouldReturnTrueWhenCountEqualsMin() {
            List<Long> techIds = List.of(1L, 2L, 3L);
            assertTrue(capabilityUseCase.isValidTechnologiesCount(techIds, 3, 20));
        }

        @Test
        @DisplayName("Should return true when count equals max boundary")
        void shouldReturnTrueWhenCountEqualsMax() {
            List<Long> techIds = LongStream.rangeClosed(1, 20).boxed().toList();
            assertTrue(capabilityUseCase.isValidTechnologiesCount(techIds, 3, 20));
        }

        @Test
        @DisplayName("Should return false when count is below min")
        void shouldReturnFalseWhenCountBelowMin() {
            List<Long> techIds = List.of(1L, 2L);
            assertFalse(capabilityUseCase.isValidTechnologiesCount(techIds, 3, 20));
        }

        @Test
        @DisplayName("Should return false when count exceeds max")
        void shouldReturnFalseWhenCountExceedsMax() {
            List<Long> techIds = LongStream.rangeClosed(1, 21).boxed().toList();
            assertFalse(capabilityUseCase.isValidTechnologiesCount(techIds, 3, 20));
        }

        @Test
        @DisplayName("Should return false when techIds is null")
        void shouldReturnFalseWhenNull() {
            assertFalse(capabilityUseCase.isValidTechnologiesCount(null, 3, 20));
        }

        @Test
        @DisplayName("Should return false when list is empty")
        void shouldReturnFalseWhenEmpty() {
            assertFalse(capabilityUseCase.isValidTechnologiesCount(Collections.emptyList(), 3, 20));
        }
    }

    @Nested
    @DisplayName("hasNoRepeatedTechnologies")
    class HasNoRepeatedTechnologiesTests {

        @Test
        @DisplayName("Should return true when all technologies are unique")
        void shouldReturnTrueWhenAllUnique() {
            List<Long> techIds = List.of(1L, 2L, 3L, 4L);
            assertTrue(capabilityUseCase.hasNoRepeatedTechnologies(techIds));
        }

        @Test
        @DisplayName("Should return false when there are duplicate technologies")
        void shouldReturnFalseWhenDuplicatesExist() {
            List<Long> techIds = Arrays.asList(1L, 2L, 2L, 3L);
            assertFalse(capabilityUseCase.hasNoRepeatedTechnologies(techIds));
        }

        @Test
        @DisplayName("Should return false when all technologies are the same")
        void shouldReturnFalseWhenAllSame() {
            List<Long> techIds = Arrays.asList(5L, 5L, 5L);
            assertFalse(capabilityUseCase.hasNoRepeatedTechnologies(techIds));
        }

        @Test
        @DisplayName("Should return false when techIds is null")
        void shouldReturnFalseWhenNull() {
            assertFalse(capabilityUseCase.hasNoRepeatedTechnologies(null));
        }

        @Test
        @DisplayName("Should return true when list has a single element")
        void shouldReturnTrueWhenSingleElement() {
            List<Long> techIds = List.of(1L);
            assertTrue(capabilityUseCase.hasNoRepeatedTechnologies(techIds));
        }

        @Test
        @DisplayName("Should return true when list is empty")
        void shouldReturnTrueWhenEmpty() {
            assertTrue(capabilityUseCase.hasNoRepeatedTechnologies(Collections.emptyList()));
        }
    }
}
