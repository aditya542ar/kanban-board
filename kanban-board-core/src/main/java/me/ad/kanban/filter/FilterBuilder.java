package me.ad.kanban.filter;

import me.ad.kanban.config.CustomMessageProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FilterBuilder<T> {

    private final CustomMessageProperties message;
    private Specification<T> specification;
    private Sort sort;
    private boolean isOR;

    public FilterBuilder(CustomMessageProperties message) {
        this.message = message;
        specification = Specification.where(null);
        isOR = false;
        sort = Sort.unsorted();
    }

    public Specification<T> getSpecification() {
        return specification;
    }

    public Sort getSort() {
        return sort;
    }

    public FilterBuilder<T> orStart() {
        isOR = true;
        return this;
    }

    public FilterBuilder<T> orEnd() {
        isOR = false;
        return this;
    }

    public FilterBuilder<T> addEqualFilter(String filterName, Optional<Object> filterOpt, Specification<T> spec) {
        filterOpt.ifPresent((filter) -> {
            specification = specification.and(spec);
        });
        return this;
    }

    public FilterBuilder<T> addInFilter(String filterName, Optional<List<? extends Object>> filterOpt, Specification<T> spec) {
        filterOpt.ifPresent((filter) -> {
            specification = specification.and(spec);
        });
        return this;
    }

    public FilterBuilder<T> addLikeFilter(String filterName, Optional<String> filterOpt, Specification<T> spec) {
        filterOpt.ifPresent((filter) -> {
            if(filter.trim().indexOf("%") == -1) {
                throw new IllegalArgumentException(
                        MessageFormat.format(message.getInvalidLikeFilterCriteria(), filterName));
            } else {
                specification = specification.and(spec);
            }
        });
        return this;
    }

    public FilterBuilder<T> addDateFilter(String filterName, Optional<String> dateOpt, Specification<T> spec) {
        dateOpt.ifPresent((date) -> {
            try {
                LocalDate d = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
                specification = specification.and(spec);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(
                        MessageFormat.format(message.getInvalidDateFilterCriteria(), filterName));
            }
        });
        return this;
    }

    public FilterBuilder<T> addSortByAndSortOrder(Optional<String> sortByOpt, Optional<String> sortOrderOpt,
                                               Set<String> validSortBy, Set<String> validSortOrder) {
        if(!sortByOpt.isPresent() && !sortOrderOpt.isPresent()) {
            sort = Sort.unsorted();
        } else if(sortByOpt.isPresent() && !sortOrderOpt.isPresent()) {
            throw new IllegalArgumentException(message.getMissingSortOrder());
        } else if(!sortByOpt.isPresent() && sortOrderOpt.isPresent()) {
            throw new IllegalArgumentException(message.getMissingSortBy());
        } else if(!validSortBy.contains(sortByOpt.get())) {
            System.out.println("message:" + message);
            System.out.println("message.getInvalidSortBy():" + message.getInvalidSortBy());
            throw new IllegalArgumentException(
                    MessageFormat
                            .format(message.getInvalidSortBy(), sortByOpt.get(), Arrays.toString(validSortBy.toArray())) );
        } else if(!validSortOrder.contains(sortOrderOpt.get())) {
            throw new IllegalArgumentException(
                    MessageFormat
                            .format(message.getInvalidSortOrder(), sortOrderOpt.get(), Arrays.toString(validSortOrder.toArray())) );
        } else {
            sort = Sort.by(Sort.Direction.fromString(sortOrderOpt.get()), sortByOpt.get());
        }
        return this;
    }

}
