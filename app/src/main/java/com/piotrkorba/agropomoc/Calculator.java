package com.piotrkorba.agropomoc;

public class Calculator {

    enum Unit {
        TONNE,
        KILOGRAM,
        tpHa,
        tpa,
        kgpHa,
        kgpa
    }

    public static double areaDensityConverter(double grammePerM2, Unit unit) {
        switch (unit) {
            case tpHa:
                return grammePerM2 * 0.01;
            case tpa:
                return grammePerM2 * 0.0001;
            case kgpHa:
                return grammePerM2 * 10;
            case kgpa:
                return grammePerM2 * 0.1;
            default:
                return grammePerM2;
        }
    }

    public static double weightConverter(double kg, Unit unit) {
        switch (unit) {
            case TONNE:
                return kg * 0.001;
            case KILOGRAM:
            default:
                return kg;
        }
    }

    public static double seedRate(double plantPopulation, double totalGrainWeight, double seedEmergency, double fieldEmergency) {
        // ((MTZ [g] * obsada[szt./m²] * 100) / (siła kiełkowania nasion [%] * siła kiełkowania pola [%]) * 0.1= ilość wysiewu w g/m²
        return (plantPopulation * totalGrainWeight * 100) / (seedEmergency * fieldEmergency) * 0.1;
    }

    public static double potentialYieldCereal(double grainsPerSpike, double totalGrainWeight, double spikesPerArea) {
        // SP [g/m²] = liczba kłosów na jednostce powierzchni [szt./m²] * (liczba ziaren w kłosie * masa 1000 ziaren [g] / 1000)
        return spikesPerArea * (grainsPerSpike * totalGrainWeight / 1000);
    }

    public static double potentialYieldLegumes(double podsPerPlant, double totalGrainWeight, double plantsPerArea, double grainsPerPod) {
        // SP [g/m²] = liczba roślin na jednostce powierzchni [szt./m²] * (liczba strąków w roślinie * liczba nasion w strąku * masa 1000 ziaren [g] / 1000)
        return plantsPerArea * (podsPerPlant * grainsPerPod * totalGrainWeight / 1000);
    }

    public static double panting(double distanceBetweenRows, double distanceBetweenPlants) {
        // (100 / odstęp między rzędami [cm]) * (100 / odstęp między roślinami [cm]) = rośliny/Ha
        return (100.0 / distanceBetweenPlants) * (100.0 / distanceBetweenRows) * 10000;
    }

    public static double weightLossOnDrying(double initialWeight, double initialMoisture, double finalMoisture) {
        // initial weight [kg] * ((100 - initial moisture [%]) / (100 - final moisture [%])) = final weigth [kg]
        return initialWeight * ((100 - initialMoisture) / (100 - finalMoisture));
    }
}
