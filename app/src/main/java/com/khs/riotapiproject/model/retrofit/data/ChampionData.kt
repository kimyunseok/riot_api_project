package com.khs.riotapiproject.model.retrofit.data

import com.google.gson.annotations.SerializedName

data class ChampionData(
    var code: Int,
    var message: String,
    @SerializedName("data")
    val data: Champion
) {
    data class Champion(
        @SerializedName("Aatrox")
        val Aatrox: ChampionInfoData,
        @SerializedName("Ahri")
        val Ahri: ChampionInfoData,
        @SerializedName("Akali")
        val Akali: ChampionInfoData,
        @SerializedName("Akshan")
        val Akshan: ChampionInfoData,
        @SerializedName("Alistar")
        val Alistar: ChampionInfoData,
        @SerializedName("Amumu")
        val Amumu: ChampionInfoData,
        @SerializedName("Anivia")
        val Anivia: ChampionInfoData,
        @SerializedName("Annie")
        val Annie: ChampionInfoData,
        @SerializedName("Aphelios")
        val Aphelios: ChampionInfoData,
        @SerializedName("Ashe")
        val Ashe: ChampionInfoData,
        @SerializedName("AurelionSol")
        val AurelionSol: ChampionInfoData,
        @SerializedName("Azir")
        val Azir: ChampionInfoData,
        @SerializedName("Bard")
        val Bard: ChampionInfoData,
        @SerializedName("Blitzcrank")
        val Blitzcrank: ChampionInfoData,
        @SerializedName("Brand")
        val Brand: ChampionInfoData,
        @SerializedName("Braum")
        val Braum: ChampionInfoData,
        @SerializedName("Caitlyn")
        val Caitlyn: ChampionInfoData,
        @SerializedName("Camille")
        val Camille: ChampionInfoData,
        @SerializedName("Cassiopeia")
        val Cassiopeia: ChampionInfoData,
        @SerializedName("Chogath")
        val Chogath: ChampionInfoData,
        @SerializedName("Corki")
        val Corki: ChampionInfoData,
        @SerializedName("Darius")
        val Darius: ChampionInfoData,
        @SerializedName("Diana")
        val Diana: ChampionInfoData,
        @SerializedName("Draven")
        val Draven: ChampionInfoData,
        @SerializedName("DrMundo")
        val DrMundo: ChampionInfoData,
        @SerializedName("Ekko")
        val Ekko: ChampionInfoData,
        @SerializedName("Elise")
        val Elise: ChampionInfoData,
        @SerializedName("Evelynn")
        val Evelynn: ChampionInfoData,
        @SerializedName("Ezreal")
        val Ezreal: ChampionInfoData,
        @SerializedName("Fiddlesticks")
        val Fiddlesticks: ChampionInfoData,
        @SerializedName("Fiora")
        val Fiora: ChampionInfoData,
        @SerializedName("Fizz")
        val Fizz: ChampionInfoData,
        @SerializedName("Galio")
        val Galio: ChampionInfoData,
        @SerializedName("Gangplank")
        val Gangplank: ChampionInfoData,
        @SerializedName("Garen")
        val Garen: ChampionInfoData,
        @SerializedName("Gnar")
        val Gnar: ChampionInfoData,
        @SerializedName("Gragas")
        val Gragas: ChampionInfoData,
        @SerializedName("Graves")
        val Graves: ChampionInfoData,
        @SerializedName("Gwen")
        val Gwen: ChampionInfoData,
        @SerializedName("Hecarim")
        val Hecarim: ChampionInfoData,
        @SerializedName("Heimerdinger")
        val Heimerdinger: ChampionInfoData,
        @SerializedName("Illaoi")
        val Illaoi: ChampionInfoData,
        @SerializedName("Irelia")
        val Irelia: ChampionInfoData,
        @SerializedName("Ivern")
        val Ivern: ChampionInfoData,
        @SerializedName("Janna")
        val Janna: ChampionInfoData,
        @SerializedName("JarvanIV")
        val JarvanIV: ChampionInfoData,
        @SerializedName("Jax")
        val Jax: ChampionInfoData,
        @SerializedName("Jayce")
        val Jayce: ChampionInfoData,
        @SerializedName("Jhin")
        val Jhin: ChampionInfoData,
        @SerializedName("Jinx")
        val Jinx: ChampionInfoData,
        @SerializedName("Kaisa")
        val Kaisa: ChampionInfoData,
        @SerializedName("Kalista")
        val Kalista: ChampionInfoData,
        @SerializedName("Karma")
        val Karma: ChampionInfoData,
        @SerializedName("Karthus")
        val Karthus: ChampionInfoData,
        @SerializedName("Kassadin")
        val Kassadin: ChampionInfoData,
        @SerializedName("Katarina")
        val Katarina: ChampionInfoData,
        @SerializedName("Kayle")
        val Kayle: ChampionInfoData,
        @SerializedName("Kayn")
        val Kayn: ChampionInfoData,
        @SerializedName("Kennen")
        val Kennen: ChampionInfoData,
        @SerializedName("Khazix")
        val Khazix: ChampionInfoData,
        @SerializedName("Kindred")
        val Kindred: ChampionInfoData,
        @SerializedName("Kled")
        val Kled: ChampionInfoData,
        @SerializedName("KogMaw")
        val KogMaw: ChampionInfoData,
        @SerializedName("Leblanc")
        val Leblanc: ChampionInfoData,
        @SerializedName("LeeSin")
        val LeeSin: ChampionInfoData,
        @SerializedName("Leona")
        val Leona: ChampionInfoData,
        @SerializedName("Lillia")
        val Lillia: ChampionInfoData,
        @SerializedName("Lissandra")
        val Lissandra: ChampionInfoData,
        @SerializedName("Lucian")
        val Lucian: ChampionInfoData,
        @SerializedName("Lulu")
        val Lulu: ChampionInfoData,
        @SerializedName("Lux")
        val Lux: ChampionInfoData,
        @SerializedName("Malphite")
        val Malphite: ChampionInfoData,
        @SerializedName("Malzahar")
        val Malzahar: ChampionInfoData,
        @SerializedName("Maokai")
        val Maokai: ChampionInfoData,
        @SerializedName("MasterYi")
        val MasterYi: ChampionInfoData,
        @SerializedName("MissFortune")
        val MissFortune: ChampionInfoData,
        @SerializedName("MonkeyKing")
        val MonkeyKing: ChampionInfoData,
        @SerializedName("Mordekaiser")
        val Mordekaiser: ChampionInfoData,
        @SerializedName("Morgana")
        val Morgana: ChampionInfoData,
        @SerializedName("Nami")
        val Nami: ChampionInfoData,
        @SerializedName("Nasus")
        val Nasus: ChampionInfoData,
        @SerializedName("Nautilus")
        val Nautilus: ChampionInfoData,
        @SerializedName("Neeko")
        val Neeko: ChampionInfoData,
        @SerializedName("Nidalee")
        val Nidalee: ChampionInfoData,
        @SerializedName("Nocturne")
        val Nocturne: ChampionInfoData,
        @SerializedName("Nunu")
        val Nunu: ChampionInfoData,
        @SerializedName("Olaf")
        val Olaf: ChampionInfoData,
        @SerializedName("Orianna")
        val Orianna: ChampionInfoData,
        @SerializedName("Ornn")
        val Ornn: ChampionInfoData,
        @SerializedName("Pantheon")
        val Pantheon: ChampionInfoData,
        @SerializedName("Poppy")
        val Poppy: ChampionInfoData,
        @SerializedName("Pyke")
        val Pyke: ChampionInfoData,
        @SerializedName("Qiyana")
        val Qiyana: ChampionInfoData,
        @SerializedName("Quinn")
        val Quinn: ChampionInfoData,
        @SerializedName("Rakan")
        val Rakan: ChampionInfoData,
        @SerializedName("Rammus")
        val Rammus: ChampionInfoData,
        @SerializedName("RekSai")
        val RekSai: ChampionInfoData,
        @SerializedName("Rell")
        val Rell: ChampionInfoData,
        @SerializedName("Renekton")
        val Renekton: ChampionInfoData,
        @SerializedName("Rengar")
        val Rengar: ChampionInfoData,
        @SerializedName("Riven")
        val Riven: ChampionInfoData,
        @SerializedName("Rumble")
        val Rumble: ChampionInfoData,
        @SerializedName("Ryze")
        val Ryze: ChampionInfoData,
        @SerializedName("Samira")
        val Samira: ChampionInfoData,
        @SerializedName("Sejuani")
        val Sejuani: ChampionInfoData,
        @SerializedName("Senna")
        val Senna: ChampionInfoData,
        @SerializedName("Seraphine")
        val Seraphine: ChampionInfoData,
        @SerializedName("Sett")
        val Sett: ChampionInfoData,
        @SerializedName("Shaco")
        val Shaco: ChampionInfoData,
        @SerializedName("Shen")
        val Shen: ChampionInfoData,
        @SerializedName("Shyvana")
        val Shyvana: ChampionInfoData,
        @SerializedName("Singed")
        val Singed: ChampionInfoData,
        @SerializedName("Sion")
        val Sion: ChampionInfoData,
        @SerializedName("Sivir")
        val Sivir: ChampionInfoData,
        @SerializedName("Skarner")
        val Skarner: ChampionInfoData,
        @SerializedName("Sona")
        val Sona: ChampionInfoData,
        @SerializedName("Soraka")
        val Soraka: ChampionInfoData,
        @SerializedName("Swain")
        val Swain: ChampionInfoData,
        @SerializedName("Sylas")
        val Sylas: ChampionInfoData,
        @SerializedName("Syndra")
        val Syndra: ChampionInfoData,
        @SerializedName("TahmKench")
        val TahmKench: ChampionInfoData,
        @SerializedName("Taliyah")
        val Taliyah: ChampionInfoData,
        @SerializedName("Talon")
        val Talon: ChampionInfoData,
        @SerializedName("Taric")
        val Taric: ChampionInfoData,
        @SerializedName("Teemo")
        val Teemo: ChampionInfoData,
        @SerializedName("Thresh")
        val Thresh: ChampionInfoData,
        @SerializedName("Tristana")
        val Tristana: ChampionInfoData,
        @SerializedName("Trundle")
        val Trundle: ChampionInfoData,
        @SerializedName("Tryndamere")
        val Tryndamere: ChampionInfoData,
        @SerializedName("TwistedFate")
        val TwistedFate: ChampionInfoData,
        @SerializedName("Twitch")
        val Twitch: ChampionInfoData,
        @SerializedName("Udyr")
        val Udyr: ChampionInfoData,
        @SerializedName("Urgot")
        val Urgot: ChampionInfoData,
        @SerializedName("Varus")
        val Varus: ChampionInfoData,
        @SerializedName("Vayne")
        val Vayne: ChampionInfoData,
        @SerializedName("Veigar")
        val Veigar: ChampionInfoData,
        @SerializedName("Velkoz")
        val Velkoz: ChampionInfoData,
        @SerializedName("Vex")
        val Vex: ChampionInfoData,
        @SerializedName("Vi")
        val Vi: ChampionInfoData,
        @SerializedName("Viego")
        val Viego: ChampionInfoData,
        @SerializedName("Viktor")
        val Viktor: ChampionInfoData,
        @SerializedName("Vladimir")
        val Vladimir: ChampionInfoData,
        @SerializedName("Volibear")
        val Volibear: ChampionInfoData,
        @SerializedName("Warwick")
        val Warwick: ChampionInfoData,
        @SerializedName("Xayah")
        val Xayah: ChampionInfoData,
        @SerializedName("Xerath")
        val Xerath: ChampionInfoData,
        @SerializedName("XinZhao")
        val XinZhao: ChampionInfoData,
        @SerializedName("Yasuo")
        val Yasuo: ChampionInfoData,
        @SerializedName("Yone")
        val Yone: ChampionInfoData,
        @SerializedName("Yorick")
        val Yorick: ChampionInfoData,
        @SerializedName("Yuumi")
        val Yuumi: ChampionInfoData,
        @SerializedName("Zac")
        val Zac: ChampionInfoData,
        @SerializedName("Zed")
        val Zed: ChampionInfoData,
        @SerializedName("Ziggs")
        val Ziggs: ChampionInfoData,
        @SerializedName("Zilean")
        val Zilean: ChampionInfoData,
        @SerializedName("Zoe")
        val Zoe: ChampionInfoData,
        @SerializedName("Zyra")
        val Zyra: ChampionInfoData
    ) {
        data class ChampionInfoData(
            @SerializedName("version")
            val version: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("key")
            val key: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("blurb")
            val blurb: String
        )
    }


}
