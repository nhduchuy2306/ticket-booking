import moment from "moment";

const getCurrentYear = () => {
    return new Date().getFullYear();
}

const formatDate = (date: string): string => {
    if (!date) {
        return '';
    }
    return moment(date).format('YYYY-MM-DD');
}

const formatToDateTime = (date: string): string => {
    if (!date) {
        return '';
    }
    return moment(date).format('YYYY-MM-DD HH:mm:ss');
}

const toIsoDateTime = (date: string): string => {
    if (!date || !moment(date).isValid()) {
        return '';
    }
    return moment(date).format('YYYY-MM-DDTHH:mm:ss');
};

const toIsoDate = (date: string | undefined | null) => {
    if (!date || !moment(date).isValid()) {
        return undefined;
    }
    return moment(date);
};

const isValidDate = (date: string): boolean => {
    return moment(date, 'YYYY-MM-DD', true).isValid() ||
            moment(date, 'YYYY-MM-DD HH:mm:ss', true).isValid();
}

export const DateUtils = {
    getCurrentYear,
    formatDate,
    formatToDateTime,
    isValidDate,
    toIsoDateTime,
    toIsoDate
}