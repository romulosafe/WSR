$(document).ready(function() {
    var search = $("#search");
    var searchButton = $("#searchButton");
        
    searchButton.mouseup(onKeyupSearchElement);
    
    search.keyup(onKeyupSearchElement);
    search.focus();

    createMusicItem({isFirst: true});

});


/*
 * Evento de teclado para #search
 * */

var onKeyupSearchElement = function(e){

    $("#search").autocomplete({
        source: getMusic,
        minlength: 2,
        delay: 700
    });

}

/*Consulta musicas na API do vagalume*/
isInLocalStorage = false;

var loadingList = [];
var getMusic = function(request,response){
	isInLocalStorage = false;
	loadingList = [];
    var fragment = $("#search").val();
    iconLoading();

    $.ajax({
            method: "POST",
            type: "jsonp",
            cache: false,
            async: false,
            url:  "http://localhost:8080/WSR/rest/service/buscar",
            data: fragment

        }).done(localUpdateMusicList);
    
    setTimeout(waitRequest, 1000);//Espera 1 segundo
    
    return false;
}

/*Faz requisição à api do vagalume*/
var waitRequest = function(){
	var fragment = $("#search").val();
	if (!isInLocalStorage) {

    	$.ajax({
            method: "GET",
            type: "jsonp",
            url:  "https://api.vagalume.com.br/search.artmus"
            + "?q=" + fragment
            + "&limit=9"
            + "&extra=relmus"
            + "&apikey={Unimusic}"

        })
        .done(updateMusicList);

	}
};

/*Busca local*/

var localUpdateMusicList = function(data){
    var musicItems = $(".music-item");
    var arrayOfResults = [];

    iconLoading();

    if(!data) return null;

    $.each(musicItems,function(index,value){
        index == 0 ? undefined:musicItems[index].remove();
    });

    $.each(data,function(index,value){

        with(data[index]){

            createMusicItem({
                musicTitle: titulo,//data.response.docs.title
                artistName: artista,
                musicId: id,
                isFirst: index == 0
            });
        }
        
        isInLocalStorage = true;
    });
}


/*
    Atualiza lista de musicas
*/

var updateMusicList = function(data){
    var musicItems = $(".music-item");
    var arrayOfResults = [];

    iconLoading();

    if(!data.response) return;

    $.each(musicItems,function(index,value){
        index == 0 ? undefined:musicItems[index].remove();
    });

    data.response.docs = data.response.docs.filter(function(doc){
        return doc.title;
    });

    $.each(data.response.docs,function(index,value){

        with(data.response.docs[index]){

            createMusicItem({
                musicTitle: title ? title:"",//data.response.docs.title
                artistName: band,
                musicId: id,
                isFirst: index == 0
            });
            //Salva lista para inserção posterior na API
            arrayOfResults.push({
                id:id,
                titulo:title,
                artista:band,
                letra:"",
                traducao:""
            });
        }
    });

    getLyricsToInsert(arrayOfResults);

}

/*
 * Configuração de music-item
 * musicTitle - Titulo da Musica
 * artistName - Nome do artista
 * musicImage - url para imagem
 * isFirst    - Indica se é o primeiro "insert" na coleção
 *
 */

/* Exemplo de chamada
 createMusicItem({
 musicTitle: 'Primeira Musica',
 musicId: 'abcde'
 artistName: 'Euzinho',
 isFirst: true
 });
 */

var createMusicItem = function(config){
    var musicItems = $(".music-item");
    var newMusicItems = musicItems.clone();
    var musicItem = newMusicItems[0];

    config.isFirst ? musicItems[0].remove() : undefined;

    musicItem.getElementsByClassName("music-title")[0].innerHTML =  config.musicTitle ? config.musicTitle : "Nenhum resultado";
    musicItem.getElementsByClassName("artist-name")[0].innerHTML =  config.artistName ? config.artistName : "...";
    musicItem.getElementsByClassName("music-img"  )[0].setAttribute("src",config.musicImage ? config.musicImage : "");
    musicItem.getElementsByClassName("music-id"   )[0].value = config.musicId ? config.musicId : "";
    musicItem.getElementsByClassName("music-link" )[0].onclick = onClickMusicItem;

    $(".music-list").append(musicItem);


}

/*
* Evento click no item da lista
*/

var onClickMusicItem = function(e){
    var musicId = this.getElementsByClassName("music-id")[0].value;

    getLyrics(musicId);

    return false

}

/*
    Obtem letra de musicas
*/

var getLyrics = function(id){
	$.ajax({
        method: "GET",
        type: "jsonp",
        url:  "http://localhost:8080/rest/service/getMusica/"+id

    })
    .done(function(data){

        var modal = $("#musicModalModel");
        console.log("");
        
        console.log(data);

        if(!data) return;

        modal[0].getElementsByClassName("music-title"  )[0].innerHTML = data.titulo;
        modal[0].getElementsByClassName("music-artist" )[0].innerHTML = data.artista;
        $(".music-content").html(data.letra);

    });
	
    $.ajax({
            method: "GET",
            type: "jsonp",
            url:  "https://api.vagalume.com.br/search.php"
            + "?musid=" + id
            + "&apikey={Unimusic}"

        })
        .done(updateLyrics);
}

/*
* Faz consulta e insere dados na API
*/

var getLyricsToInsert = function(arrayData){

    $.each(arrayData,function(index,value){
        //Busca letra de musica
        $.ajax({
                method: "GET",
                type: "jsonp",
                url:  "https://api.vagalume.com.br/search.php"
                + "?musid=" + value.id
                + "&apikey={Unimusic}"

        }).done(function(info){

                value.letra = info.mus[0].text.replace(/\n/g,"<br/>");
             
                $.ajax({
                        method: "POST",
                        //async: true,
                        type: "jsonp",
                        url:  "http://localhost:8080/WSR/rest/service/cadastrar",
                        data: JSON.stringify(value)

                    });

            });
    });

}


/*
*   Atualiza Modal com Musica selecionada
*/

var updateLyrics = function(data){

    var modal = $("#musicModalModel");

    if(data.type != 'exact') return;

    modal[0].getElementsByClassName("music-title"  )[0].innerHTML = data.art.name;
    modal[0].getElementsByClassName("music-artist" )[0].innerHTML = data.mus[0].name;
    $(".music-content").html(data.mus[0].text.replace(/\n/g,"<br/>"));

}

/*
* Icone de loading
* */

var iconLoading = function(){

    $('#ico-search')
        .toggleClass('glyphicon-refresh-animate')
        .toggleClass('glyphicon-refresh')
        .toggleClass('glyphicon');
    return false;
};
