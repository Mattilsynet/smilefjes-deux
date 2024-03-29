/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.clj*"],
  theme: {
    extend: {
      colors: {
        'furu': {
          '100': '#F5FAF8',
          '200': '#EBF4F1',
          '300': '#DAECE5',
          '400': '#9DDDC5',
          '500': '#68B096',
          '600': '#46866F',
          '700': '#2F5A4B',
          '800': '#1A322A',
          '900': '#0E1B16'
        },
        'granskog': {
          '100': '#F1FDFE',
          '200': '#E2FBFD',
          '300': '#CBF8FB',
          '400': '#84EFF6',
          '500': '#28E3F0',
          '600': '#0DAEBA',
          '700': '#08676E',
          '800': '#054449', // standard
          '900': '#032426'
        },
        'lav': '#E2F1DF',
        'hvit': '#fff',
        'gåsunge': {
          '200': '#FAF6F3', // standard
          '300': '#F4ECE6' // fra skissene, forsøksvis plassert her
        },
        'sommerdag': {
          '100': '#FEF9F0',
          '200': '#FEF4E2',
          '300': '#FDEBC9',
          '400': '#F6D683',
          '500': '#F5AD23',
          '600': '#C38309',
          '700': '#845906',
          '800': '#493103',
          '900': '#231906'
        }
      }
    }
  }
}
