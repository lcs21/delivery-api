package com.deliverytech.delivery_api.services.impl;

import java.util.List;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.services.ClienteService;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        // Validar email único
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }
        // Converter DTO para entidade
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        // Salvar cliente
        Cliente clienteSalvo = clienteRepository.save(cliente);
        // Retornar DTO de resposta
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        // Buscar cliente existente
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado: " + id));
        // Validar dados do cliente
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new BusinessException("Nome do cliente é obrigatório");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new BusinessException("Email do cliente é obrigatório");
        }
        // Atualizar dados do cliente
        clienteExistente.setNome(dto.getNome());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setTelefone(dto.getTelefone());
        clienteExistente.setEndereco(dto.getEndereco());
        // Salvar cliente atualizado
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        // Retornar DTO de resposta
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO ativarDesativarCliente(Long id) {
        // Buscar cliente existente
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado: " + id));
        // Inverter status de ativo
        clienteExistente.setAtivo(!clienteExistente.getAtivo());
        // Salvar cliente atualizado
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        // Retornar DTO de resposta
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        // Buscar cliente por ID
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado: " + id));
        // Converter entidade para DTO
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO buscarPorEmail(String email) {
        // Buscar cliente por email
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com email: " + email));
        // Converter entidade para DTO
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public List<ClienteResponseDTO> listarAtivos() {
        // Buscar clientes ativos
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        // Converter lista de entidades para lista de DTOs
        return clientesAtivos.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .toList();
    }
    //buscar por nome
    @Override
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        // Buscar clientes por nome
        List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);
        // Converter lista de entidades para lista de DTOs
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .toList();
    }
}