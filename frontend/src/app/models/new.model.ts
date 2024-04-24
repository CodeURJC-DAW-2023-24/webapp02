import { Locale } from 'date-fns';

export interface New {
    id?: number;
    title: string;
    description: string;
    date: Locale;
    link: string;
}