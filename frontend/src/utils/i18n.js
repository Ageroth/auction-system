import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import detector from 'i18next-browser-languagedetector';
import translationEN from '../translations/en/translation.json';
import translationPL from '../translations/pl/translation.json';

const resources = {
    en: {
        translation: translationEN
    },
    pl: {
        translation: translationPL
    }
};

const options = {
    order: ['navigator']
}

i18n
    .use(detector)
    .use(initReactI18next)
    .init({
        resources,
        detection: options,
        fallbackLng: 'en',
        debug: true,

        interpolation: {
            escapeValue: false,
        }
    });

export default i18n;