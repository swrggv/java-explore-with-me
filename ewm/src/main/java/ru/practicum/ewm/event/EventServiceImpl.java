package ru.practicum.ewm.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.client.ClientService;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ModelNotFoundException;
import ru.practicum.ewm.exception.NoRootException;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ClientService clientService;
    private final UserRepository userRepository;

    private final EntityManager entityManager;

    public EventServiceImpl(EventRepository eventRepository,
                            CategoryRepository categoryRepository, ClientService clientService,
                            UserRepository userRepository, EntityManager entityManager) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public EventFullDto publishEventAdmin(long eventId) {
        Event event = fromOptionalToEvent(eventId);
        boolean checkDate = checkDateStartEventAndDatePublish(event);
        boolean checkState = checkStateEvent(event);
        if (checkDate && checkState) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
            event = eventRepository.save(event);
            return EventMapper.toFullEventDtoFromEvent(event, getViews(event.getId()));
        } else {
            throw new BadRequestException("Impossible to publish event",
                    "Date or state impede publishing");
        }
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        Event event = fromOptionalToEvent(eventId);
        if (event.getState() == State.PENDING) {
            event.setState(State.CANCELED);
            return EventMapper.toFullEventDtoFromEvent(event, getViews(event.getId()));
        } else {
            throw new BadRequestException("Impossible reject event",
                    String.format("Event %d already published or rejected", eventId));
        }
    }

    @Override
    @Transactional
    public EventFullDto changeEventAdmin(long eventId, AdminUpdateEventRequest adminEvent) {
        Event oldEvent = fromOptionalToEvent(eventId);
        Event result = patchAdmin(oldEvent, adminEvent);
        result = eventRepository.save(result);
        return EventMapper.toFullEventDtoFromEvent(result, getViews(result.getId()));
    }

    @Override
    public List<EventFullDto> getEventAdmin(List<Long> users,
                                            List<State> states,
                                            List<Integer> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            int from,
                                            int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        List<Predicate> predicates = getPredicatesAdmin(users, states, categories, rangeStart, rangeEnd, criteriaBuilder, root);
        int page = pageNumber(from, size);

        /*if (users != null) {
            predicates.add(criteriaBuilder.or(root.get("initiator").in(users)));
        }
        if (states != null) {
            predicates.add(criteriaBuilder.or(root.get("state").in(states)));
        }
        if (categories != null) {
            predicates.add(criteriaBuilder.or(root.get("category").in(categories)));
        }*/

        criteriaQuery.select(root).where(predicates.toArray(Predicate[]::new)).orderBy(criteriaBuilder.desc(root.get("id")));
        List<Event> events = entityManager.createQuery(criteriaQuery)
                .setMaxResults(size)
                .setFirstResult(page)
                .getResultList();
        return transferToFullEventDto(events);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               int from,
                                               int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        List<Predicate> predicates = getPredicatesPublic(text, categories, paid, rangeStart, rangeEnd, criteriaBuilder, root);
        List<Event> events;
        //rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        int page = pageNumber(from, size);

        /*predicates.add(criteriaBuilder.equal(root.get("state"), State.PUBLISHED));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));*/


        /*if (text != null) {
            Predicate one = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate two = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
            Predicate res = criteriaBuilder.or(one, two);
            predicates.add(res);
        }
        if (categories != null) {
            predicates.add(criteriaBuilder.or(root.get("category").in(categories)));
        }
        if (paid != null) {
            predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
        }
        if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }*/

        criteriaQuery.select(root).where(predicates.toArray(Predicate[]::new));

        if (sort.equals("EVENT_DATE")) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("eventDate")));
        }

        events = entityManager.createQuery(criteriaQuery)
                .setMaxResults(size)
                .setFirstResult(page)
                .getResultList();
        if (onlyAvailable) {
            events = events.stream()
                    .filter(x -> (x.getParticipantLimit() - x.getConfirmedRequests()) > 0)
                    .collect(Collectors.toList());
        }
        if (sort.equals("VIEWS")) {
            return events.stream()
                    .map(x -> EventMapper
                            .toShortEventDtoFromEvent(x, getViews(x.getId())))
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return transferToShortEventDto(events);
    }

    @Override
    public EventFullDto getEventByIdPublic(long id) {
        Event event = fromOptionalToEvent(id);
        if (event.getState() == State.PUBLISHED) {
            return EventMapper.toFullEventDtoFromEvent(event, getViews(event.getId()));
        } else {
            throw new NoRootException("Impossible to get event",
                    String.format("This event %d isn't published", id));
        }
    }

    @Override
    public List<EventShortDto> getAllEventsPrivate(Long userId, int from, int size) {
        int page = pageNumber(from, size);
        List<Event> events = eventRepository.findByInitiator(userId, PageRequest.of(page, size));
        return transferToShortEventDto(events);
    }

    @Override
    @Transactional
    public EventFullDto patchEventPrivate(Long userId, UpdateEventRequest updateEvent) {
        checkDate(updateEvent.getEventDate());
        Event event = fromOptionalToEvent(updateEvent.getEventId());
        checkInitiator(userId, event);
        checkState(event);
        patchPrivate(event, updateEvent);
        event = eventRepository.save(event);
        return EventMapper.toFullEventDtoFromEvent(event, getViews(event.getId()));
    }

    @Override
    @Transactional
    public EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto) {
        checkDate(newEventDto.getEventDate());
        User user = fromOptionalToUser(userId);
        Category category = fromOptionalToCategory(newEventDto.getCategory());
        Event event = EventMapper.toEventFromNewEventDto(newEventDto, category, user);
        event = eventRepository.save(event);
        return EventMapper.toFullEventDtoFromEvent(event, getViews(event.getId()));
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        return EventMapper.toFullEventDtoFromEvent(fromOptionalToEvent(eventId), getViews(eventId));
    }

    @Override
    public List<EventFullDto> getEventsByIds(List<Long> events) {
        List<Event> result = eventRepository.findAllById(events);
        return transferToFullEventDto(result);
    }

    private List<EventFullDto> transferToFullEventDto(List<Event> events) {
        return events.stream()
                .map(x -> EventMapper.toFullEventDtoFromEvent(x, getViews(x.getId())))
                .collect(Collectors.toList());
    }

    private List<EventShortDto> transferToShortEventDto(List<Event> events) {
        return events.stream()
                .map(x -> EventMapper
                        .toShortEventDtoFromEvent(x, getViews(x.getId())))
                .collect(Collectors.toList());
    }

    private Integer getViews(long id) {
        return clientService.getStats("/events/" + id);
    }

    private void checkState(Event event) {
        if (event.getState() == State.PUBLISHED) {
            throw new BadRequestException("Impossible to patch event",
                    String.format("Event %d already published", event.getId()));
        }
    }

    private void checkInitiator(Long userId, Event event) {
        if (event.getInitiator().getId() != userId) {
            throw new NoRootException("Impossible to patch event",
                    String.format("User %d is not creator event %d", userId, event.getId()));
        }
    }

    private Event patchPrivate(Event event, UpdateEventRequest updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = fromOptionalToCategory(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        event.setState(State.PENDING);
        return event;
    }

    private void checkDate(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Impossible to create event",
                    "Event date is before than 2 hours from now");
        }
    }

    private int pageNumber(int from, int size) {
        return from / size;
    }

    private Event patchAdmin(Event oldEvent, AdminUpdateEventRequest adminEvent) {
        if (adminEvent.getPaid() != null) {
            oldEvent.setPaid(adminEvent.getPaid());
        }
        if (adminEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(adminEvent.getRequestModeration());
        }
        if (adminEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(adminEvent.getAnnotation());
        }
        if (adminEvent.getEventDate() != null) {
            oldEvent.setEventDate(adminEvent.getEventDate());
        }
        if (adminEvent.getCategory() != null) {
            Category category = fromOptionalToCategory(adminEvent.getCategory());
            oldEvent.setCategory(category);
        }
        if (adminEvent.getDescription() != null) {
            oldEvent.setDescription(adminEvent.getDescription());
        }
        if (adminEvent.getLocation() != null) {
            oldEvent.setLocation(adminEvent.getLocation());
        }
        if (adminEvent.getTitle() != null) {
            oldEvent.setTitle(adminEvent.getTitle());
        }
        if (adminEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(adminEvent.getParticipantLimit());
        }
        return oldEvent;
    }

    private boolean checkStateEvent(Event event) {
        return event.getState() == State.PENDING;
    }

    private boolean checkDateStartEventAndDatePublish(Event event) {
        LocalDateTime publishDate = LocalDateTime.now();
        LocalDateTime startDate = event.getEventDate();
        return startDate.isAfter(publishDate.plusHours(1));
    }

    private Event fromOptionalToEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new ModelNotFoundException("Impossible to find event",
                String.format("Event %d not found", eventId)));
    }

    private User fromOptionalToUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException("User not found",
                        String.format("User %d not found", userId)));
    }

    private Category fromOptionalToCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ModelNotFoundException("Category not found",
                        String.format("Category %d not found", categoryId)));
    }

    private List<Predicate> getPredicatesAdmin(List<Long> users,
                                               List<State> states,
                                               List<Integer> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               CriteriaBuilder criteriaBuilder,
                                               Root<Event> root) {
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        if (users != null) {
            predicates.add(criteriaBuilder.or(root.get("initiator").in(users)));
        }
        if (states != null) {
            predicates.add(criteriaBuilder.or(root.get("state").in(states)));
        }
        if (categories != null) {
            predicates.add(criteriaBuilder.or(root.get("category").in(categories)));
        }
        return predicates;
    }

    private List<Predicate> getPredicatesPublic(String text,
                                                List<Integer> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                CriteriaBuilder criteriaBuilder,
                                                Root<Event> root) {
        List<Predicate> predicates = new ArrayList<>();
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        predicates.add(criteriaBuilder.equal(root.get("state"), State.PUBLISHED));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        if (text != null) {
            Predicate one = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate two = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
            Predicate result = criteriaBuilder.or(one, two);
            predicates.add(result);
        }
        if (categories != null) {
            predicates.add(criteriaBuilder.or(root.get("category").in(categories)));
        }
        if (paid != null) {
            predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
        }
        if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        return predicates;
    }
}
