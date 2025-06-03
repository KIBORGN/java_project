Проект "E-Asigurări mașini"
Техническое задание
1. Общая информация
Страховая компания хочет приобрести приложение E-Asigurări mașini для помощи сотрудникам при заключении договоров страхования автомобилей.
2. Описание задачи
Сотрудник выбирает марку и год выпуска автомобиля клиента.
Система отображает модели, выпущенные в указанном году. Сотрудник выбирает модель.
Приложение отображает характеристики автомобиля и рассчитывает:
Индекс риска: выше для машин с мощными двигателями и ещё выше для мотоциклов и экипажей.
Индекс загрязнения: зависит от типа двигателя (дизель, бензин, электрический), экологического класса (Euro1, Euro2, Euro3 и т. д.) и возраста автомобиля.
Учитывается история клиента: если за последние 5 лет было несколько аварий, страховой взнос увеличивается.
Клиент выбирает типы рисков для страхования (авария, землетрясение, пожар, кража и т. д.).
Система вычисляет страховую сумму с учётом:
Исходной стоимости автомобиля.
Возраста (депрециация).
Установленных опций (радио, MP3-плеер, кондиционер и т. д.).
Выбранных типов рисков (для каждого вида применяются коэффициенты).
Индексов риска и загрязнения.
Клиент выбирает периодичность оплаты: ежемесячно, раз в полгода (скидка 5 %) или раз в год (скидка 10 %).
После расчёта окончательного взноса система печатает договор для подписания сотрудником и клиентом.
Оплата фиксируется сотрудником: поиск договора по уникальному номеру контракта и сохранение информации о платеже.
Если у клиента появляется другой автомобиль, текущий договор аннулируется и заключается новый (данные старого контракта обновляются).

3. Требования к приложению
Диаграмма UML классов.
Реализация на Java:
Все функции из описания должны быть реализованы.
Сохранение данных во внешних файлах (JSON, CSV или сериализация), чтобы информация не терялась при перезапуске.
Использовать Swing для графического интерфейса.
Вложенные внутренние классы для обработки событий.
Для уведомлений и сообщений — динамические коллекции объектов.
Без генераторов кода: весь код писать «вручную» в IDE.
Среда разработки:
IDE — Visual Studio Code.
Проект Java создаётся стандартно:
В папке src/ создаётся пакет (папка) для каждого класса, например Client.
Внутри src/Client/Client.java указывается package Client; и код класса.
В App.java (в корне src) подключаются классы через import Client.Client;, создаётся графический интерфейс и точка входа.


classDiagram
    class Employee {
        - employeeId: String
        - name: String
        + createContract(client: Client, vehicle: Vehicle): InsuranceContract
        + recordPayment(contractNumber: String, amount: double)
    }

    class Client {
        - clientId: String
        - name: String
        - accidentHistory: AccidentHistory
        + getHistory(): AccidentHistory
    }

    class Vehicle {
        - vin: String
        - make: String
        - model: String
        - year: int
        - basePrice: double
        - engine: Engine
        - options: List<InsuranceOption>
        + calculateDepreciation(): double
    }

    class Engine {
        - type: EngineType
        - euroClass: String
        + getPollutionIndex(): double
    }

    class AccidentHistory {
        - accidentsLast5Years: int
        + getRiskFactor(): double
    }

    class InsuranceOption {
        - name: String
        - cost: double
    }

    class Coverage {
        - type: CoverageType
        - riskFactor: double
    }

    class InsuranceContract {
        - contractNumber: String
        - dateIssued: LocalDate
        - paymentSchedule: PaymentSchedule
        - client: Client
        - vehicle: Vehicle
        - insuredSum: double
        - discount: double
        + calculatePremium(): double
    }

    class Payment {
        - paymentId: String
        - date: LocalDate
        - amount: double
    }

    %% Enumerations
    enum EngineType { DIESEL, PETROL, ELECTRIC }
    enum CoverageType { ACCIDENT, EARTHQUAKE, FIRE, THEFT }
    enum PaymentSchedule { MONTHLY, SEMIANNUAL, ANNUAL }

    %% Связи
    Employee "1" --> "*" InsuranceContract : issues
    Client "1" --> "*" InsuranceContract : holds
    Client "1" --> "1" AccidentHistory : has
    Vehicle "1" --> "*" InsuranceOption : has
    Vehicle "1" --> "1" Engine : powered by
    InsuranceContract "1" --> "1" Vehicle : covers
    InsuranceContract "1" --> "*" Coverage : includes
    InsuranceContract "1" --> "*" Payment : records