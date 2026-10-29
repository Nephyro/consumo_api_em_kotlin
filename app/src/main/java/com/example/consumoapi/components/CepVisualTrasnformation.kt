package com.example.consumoapi.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Pega apenas os primeiros 8 caracteres (caso passe algo a mais)
        val trimmed = if (text.text.length > 7) text.text.substring(0..7) else text.text
        var out = ""    // Coloca um hífen a cada tanto caracteres

        // Coloca um hífen entre os 5 primeiros caracteres
        for (i in trimmed.indices) {    // o índice do caractere a ser inserido
            out += trimmed[i]           // o caractere a ser inserido
            if (i == 4) out += "-"      // inserindo o hífen entre os 5 primeiros caracteres
        }

        // Variável para mapear o texto original para o texto transformado
        // O OffeseMap é um objeto usado para mapear o texto transformado para o texto original
        val cepOffsetTranslator = object : OffsetMapping {
            // Override é usado para sobrescrever um método da classe mãe
            override fun originalToTransformed(offset: Int): Int {
                val clampedOffset = offset.coerceIn(0, trimmed.length)                      // Garante que o offset não seja negativo ou maior que o tamanho do texto
                return if (clampedOffset <= 4) clampedOffset else clampedOffset + 1         // Adiciona um para o hífen entre os 5 primeiros caracteres
            }

            // Mapeia o texto transformado para o texto original
            override fun transformedToOriginal(offset: Int): Int {
                val clampedOffset = offset.coerceIn(0, trimmed.length)
                return if (clampedOffset <= 5) clampedOffset else clampedOffset - 1         // Remove o hífen
            }
        }

        // Retorna um objeto TransformedText que contém o texto transformado e o OffsetMapping
        return TransformedText(AnnotatedString(out), cepOffsetTranslator)
    }
}